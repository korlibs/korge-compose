import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korma.geom.*

suspend fun main() = Korge(scaleAnchor = Anchor.TOP_LEFT, scaleMode = ScaleMode.NO_SCALE, clipBorders = false, forceRenderEveryFrame = false) {
    sceneContainer().changeTo({ MainComposable() })
}
