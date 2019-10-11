package uploader

import com.google.gson.Gson
import common.InputFile
import common.Output
import common.Writer
import okhttp3.*
import uploader.model.CreateUploadUrlResponse
import uploader.model.SubmissionResponse
import uploader.model.UploadFileResponse
import java.io.ByteArrayOutputStream


class Uploader {

    private val authorizationToken: String
        get() {
            val environmentToken = System.getenv("TOKEN")
            return if (environmentToken.isNullOrBlank()) DEFAULT_TOKEN else environmentToken
        }

    private val gson = Gson()
    private val client = OkHttpClient()

    fun upload(solutions: List<Pair<InputFile, Output>>) {
        print("Uploading solutions: ")

        val sourcesBlob = uploadFile("sources.zip", packToZip("."))
        val solutionBlobs = solutions.map { (inputFile, output) ->
            val byteArrayStream = ByteArrayOutputStream()
            Writer.write(output, byteArrayStream)
            inputFile to uploadFile(inputFile.fileName, byteArrayStream.toByteArray())
        }

        solutionBlobs.forEach { (inputFile, solutionBlob) ->
            submitSolution(inputFile.dataSetId, solutionBlob, sourcesBlob)
        }

        println()
    }

    private fun submitSolution(
        dataSet: String,
        submissionBlob: String,
        sourcesBlob: String
    ) {
        val url = HttpUrl.parse("https://hashcode-judge.appspot.com/api/judge/v1/submissions")!!.newBuilder()
            .addQueryParameter("dataSet", dataSet)
            .addQueryParameter("submissionBlobKey", submissionBlob)
            .addQueryParameter("sourcesBlobKey", sourcesBlob)
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", authorizationToken)
            .post(RequestBody.create(null, ""))
            .build()

        val response = client.newCall(request).execute().body()!!
            .string()
            .parseJson<SubmissionResponse>()

        if (response.id != null) {
            print("+")
        } else {
            print("-")
        }
    }

    private fun uploadFile(fileName: String, content: ByteArray): String {
        // Create upload url
        val request = Request.Builder()
            .url("https://hashcode-judge.appspot.com/api/judge/v1/upload/createUrl")
            .addHeader("Authorization", authorizationToken)
            .build()

        val response = client.newCall(request).execute().body()!!
            .string()
            .parseJson<CreateUploadUrlResponse>()

        val uploadUrl = response.value

        val sendFileBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", fileName,
                RequestBody.create(MediaType.get("application/octet-stream"), content)
            )
            .build()

        val uploadRequest = Request.Builder()
            .url(uploadUrl)
            .post(sendFileBody)
            .build()

        val uploadResponse = client.newCall(uploadRequest).execute().body()!!
            .string()
            .parseJson<UploadFileResponse>()

        return uploadResponse.file.first()
    }

    private inline fun <reified T> String.parseJson(): T {
        try {
            return gson.fromJson<T>(this, T::class.java)
        } catch (exception: Exception) {
            println("Error parsing json for $this")
            exception.printStackTrace()
            throw exception
        }
    }

    private companion object {
        private const val DEFAULT_TOKEN =
            "Bearer ya29.GosBvgbJ7SFMvGYIk_qHICP4tP3PRkXFkFEoCmBYQEkXeTftj4jqxx7oWPQDSIFMntg3pPqeFyajDnSVTtEwZOetqKqqRsB3_jJL_Act4PaPnIhRDcqlRE0qpFzh3YkvVBu8OQdvZydi3cW3jvguzR4hTC_O4mBpZEHa7nUelf7bO7KLfdy46hhTNLCvuQ"

    }
}