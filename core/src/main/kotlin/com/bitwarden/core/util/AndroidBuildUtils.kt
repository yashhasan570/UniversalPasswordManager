@file:OmitFromCoverage

package .core.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import .annotation.OmitFromCoverage

/**
 * Returns true if the current OS build version is below the provided [version].
 *
 * @see Build.VERSION_CODES
 */
@ChecksSdkIntAtLeast(parameter = 0)
fun isBuildVersionAtLeast(
    version: Int,
): Boolean = Build.VERSION.SDK_INT >= version
