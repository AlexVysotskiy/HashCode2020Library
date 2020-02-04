package solver.lukaville;

import common.Input;
import common.Ride;
import org.jocl.*;

import java.util.List;

import java.util.Arrays;

import static org.jocl.CL.*;

public class GpuSolver {

    private cl_kernel kernel;
    private cl_command_queue commandQueue;
    private cl_program program;
    private cl_context context;

    private cl_mem srcMemCommonParams;
    private cl_mem srcMemRides;
    private cl_mem srcMemGreedyParams;
    private cl_mem dstResultScoresMem;

    private int workItems;
    private float[] srcArrayGreedyParams;

    private int[] resultScoresArray;
    private Pointer resultScoresPointer;

    public void initialize(Input input, int workItems, int greedyParamsCount, int[] resultBuffer) {
        this.workItems = workItems;

        // todo fill from input
        final int[] srcArrayCommonParams = new int[6];
        srcArrayCommonParams[0] = input.getGridSize().getWidth();
        srcArrayCommonParams[1] = input.getGridSize().getHeight();
        srcArrayCommonParams[2] = input.getVehicles();
        srcArrayCommonParams[3] = input.getRides().size();
        srcArrayCommonParams[4] = input.getBonus();
        srcArrayCommonParams[5] = input.getTimeLimit();

        final int[] srcArrayRides = new int[6 * input.getRides().size()];
        final List<Ride> rides = input.getRides();
        for (int i = 0; i < rides.size(); i += 6) {
            final Ride ride = rides.get(i);
            srcArrayRides[i] = ride.getStart().getX();
            srcArrayRides[i + 1] = ride.getStart().getY();
            srcArrayRides[i + 2] = ride.getEnd().getX();
            srcArrayRides[i + 3] = ride.getEnd().getY();
            srcArrayRides[i + 4] = ride.getStartTime();
            srcArrayRides[i + 5] = ride.getEndTime();
        }

        srcArrayGreedyParams = new float[greedyParamsCount * workItems];

        Pointer srcArrayCommonParamsPointer = Pointer.to(srcArrayCommonParams);
        Pointer srcArrayRidesPointer = Pointer.to(srcArrayRides);
        Pointer srcArrayGreedyParamsPointer = Pointer.to(srcArrayGreedyParams);

        resultScoresArray = resultBuffer;
        resultScoresPointer = Pointer.to(resultScoresArray);

        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        cl_queue_properties properties = new cl_queue_properties();
        commandQueue = clCreateCommandQueueWithProperties(
                context, device, properties, null);

        // Allocate the memory objects for the input- and output data
        srcMemCommonParams = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * srcArrayCommonParams.length, srcArrayCommonParamsPointer, null);
        srcMemRides = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * srcArrayRides.length, srcArrayRidesPointer, null);
        srcMemGreedyParams = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * srcArrayGreedyParams.length, srcArrayGreedyParamsPointer, null);

        dstResultScoresMem = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_int * workItems, null, null);

        // Create the program from the source code
        program = clCreateProgramWithSource(context,
                1, new String[]{GpuSolverKernel.source}, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(program, "solverKernel", null);

        System.out.println("MAX_WORK_GROUP_SIZE: " + getDeviceLong(device, CL_DEVICE_MAX_WORK_GROUP_SIZE));
    }

    private long getDeviceLong(cl_device_id device, int paramName) {
        return getDeviceLongs(device, paramName, 1)[0];
    }

    private long[] getDeviceLongs(cl_device_id device, int paramName, int numValues) {
        long values[] = new long[numValues];
        clGetDeviceInfo(device, paramName, Sizeof.cl_long * numValues, Pointer.to(values), null);
        return values;
    }

    public void solve(float[][] params, int[] scoresOutput) {
        int k = 0;
        for (final float[] oneWorkerParams : params) {
            for (float oneWorkerParam : oneWorkerParams) {
                srcArrayGreedyParams[k] = oneWorkerParam;
                k++;
            }
        }

        // Set the arguments for the kernel
        int a = 0;
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(srcMemCommonParams));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(srcMemRides));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(srcMemGreedyParams));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(dstResultScoresMem));

        // Set the work-item dimensions
        long global_work_size[] = new long[]{workItems};

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, null, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(commandQueue, dstResultScoresMem, CL_TRUE, 0,
                workItems * Sizeof.cl_int, resultScoresPointer, 0, null, null);

        System.arraycopy(resultScoresArray, 0, scoresOutput, 0, resultScoresArray.length);

        // Verify the result
        System.out.println("Read results: " + Arrays.toString(resultScoresArray));
    }

    public void terminate() {
        // Release kernel, program, and memory objects
        clReleaseMemObject(srcMemCommonParams);
        clReleaseMemObject(srcMemRides);
        clReleaseMemObject(dstResultScoresMem);
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
    }
}
