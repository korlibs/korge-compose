import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*

suspend fun main() = Korge(
    displayMode = KorgeDisplayMode(ScaleMode.NO_SCALE, Anchor.TOP_LEFT, clipBorders = false),
    forceRenderEveryFrame = false
).start {
    sceneContainer().changeTo({ MainComposable() })
}
