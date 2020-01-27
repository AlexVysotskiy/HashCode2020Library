import common.InputFile
import common.Solver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(

    )

    val inputFiles = listOf(
        InputFile("inputs/a_example.in", "AMmfu6YRH6u5gPB589fYUnEmUT9HCQgDjqHODgX0I2PjINRhoQNrcv6_PbQJxwC6oG5cqGb0oIdg07qIJlcv58Ut9aAp1NXk4oJBmJQOjThybtqwqkfB7WvOV6PdbVqhHZ8J1HxXpgXTFsueZd-nINzfT9l4_lr59nNDjOBxBkHl3JOtNzbIO05xh9FqkvclGry06FFtJxs7vcJwwWSSQQ8O24bpMhN10Q/ALBNUaYAAAAAXi8r2OQtk8Bv_-9Zq28CBjuJDTOWGnkV"),
        InputFile("inputs/b_small.in", "AMmfu6ZmF-MxrgIoXvX-sBlGxEHb3Ctvz_ZEjCwouDJRQjg3lDJNqb5ZeHQhNmgteKxYc_niojRtyVUaXMhQgc4k88TeDMLv2Q6E4SqtIVhkGJTPxgUoFpM9ZkTjf2LqOp0O5VxPiX_3z_b4WQr1Igv70CuaONFaF-clR-lUcDS-svaQeWlVYVslE8R_TQfaWpsPKehBnxsCGevK7nhhgmhQuV4E2GbgLg/ALBNUaYAAAAAXi8r3XvemPxcTFR5-PXzn98D7yLI5d2x"),
        InputFile("inputs/c_medium.in", "AMmfu6aHIJ1g1T0lfUd9-RByEUvzFdJm8FLQG47--D7YNvLDZHVx2syclQ4oAzai_6J_LvEJeLOxehep1g7_XWjrHNI6w8J5x3OpcCW2VfRKN43IHbuYBYqgf8H7mKotfuFeo_axvVhyaEOv6iMy4It-VCYmcabspjIiqrwfP59H57Hv7IjdDlej8_Eyg7aAZ17Wig9u7cjPCI7qEWnY_ddQEyb2DfxSXQ/ALBNUaYAAAAAXi8r4Vq7xNWZw6cLjnlc0x94eGjdjxNL"),
        InputFile("inputs/d_quite_big.in", "AMmfu6bT6XER_AKg15d7ZDID4NrhISBMRQUjdc1Fv0bpxPdieLgaCkIWCgUDHUeiVU8IcZXd06wBqf0qH_VjqQS2PCr0iugvEjHpnsxX3aS-MlguthMcQiqwaWlUDGynEJWtiDhVjgx1mnhK_xKp_bp2dTx_EM-RpSdh0XakNkikkDFo4NY3bhR0Vq6z0tmAAHH8dtkdctnJ0VUH-4S4HbwMfNAtbekykw/ALBNUaYAAAAAXi8r54O7tF3yjayPSOPbvAYF_ymXvTuG"),
        InputFile("inputs/e_also_big.in", "AMmfu6Z4iLIz68L-OVnHwdoaMV9C6UkaCBqgmJeMpUK5jpJwn_PNEegF1Zc1uOX2tUAnml_9rLMrxSakbPBObBN9Oh2duUKtmdOpK5u6IgW7B2T2p_uY45atEMd-OkeYJXp1DGTOxbDxSEnGblbhRrfcSUCde4e6m2SHAqViVQL8anp7KbhkDUCrSVIy01l68tCyYaAYZXWb1lXCGToQYDtzFSfngkeAPg/ALBNUaYAAAAAXi8r6vRmt7ycHb4LOH4afpV74BSL9wvu")
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
