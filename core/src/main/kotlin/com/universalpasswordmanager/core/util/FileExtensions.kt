package com.universalpasswordmanager.core.util

import com.universalpasswordmanager.annotation.OmitFromCoverage
import java.io.File

/**
 * A helper function for creating a file from a path.
 */
@OmitFromCoverage
fun fileOf(path: String): File = File(path)
