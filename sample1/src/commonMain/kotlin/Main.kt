import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.ControlledComposition
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.ObserverHandle
import androidx.compose.runtime.snapshots.Snapshot
import com.soywiz.klock.DateTime
import com.soywiz.korge.Korge
import com.soywiz.korge.input.onClick
import com.soywiz.korge.ui.UIButton
import com.soywiz.korge.ui.UIHorizontalStack
import com.soywiz.korge.ui.UIText
import com.soywiz.korge.ui.UIVerticalStack
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.View
import com.soywiz.korge.view.Views
import com.soywiz.korge.view.onNextFrame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun main() = Korge(width = 256, height = 64) {
    setComposeContent {
        var count by remember { mutableStateOf(0) }
        VStack {
            Text("$count")
            HStack {
                Button("-") { count-- }
                Button("+") { count++ }
            }
        }
    }
}

// We would implement an Applier class like the following, which would teach compose how to
// manage a tree of Nodes.
// https://developer.android.com/reference/kotlin/androidx/compose/runtime/Applier
class NodeApplier(root: View) : AbstractApplier<View>(root) {
    override fun insertTopDown(index: Int, instance: View) {
        println("insertTopDown[$index]: $instance")
        val container = current as Container
        container.addChildAt(instance, container.numChildren - 1 - index)
        //current.children.add(index, instance)
    }

    override fun insertBottomUp(index: Int, instance: View) {
        println("insertBottomUp[$index]: $instance")
        (current as? Container?)?.addChildAt(instance, index)
        // Ignored as the tree is built top-down.
    }

    override fun remove(index: Int, count: Int) {
        println("remove[$index]..$count")
        (current as? Container?)?.removeChildAt(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        println("move: $from -> $to [$count]")
        repeat(count) { (current as? Container?)?.swapChildrenAt(from + it, to + it) }
    }

    override fun onClear() {
        println("onClear")
        (current as? Container?)?.removeChildren()
    }
}

// A function like the following could be created to create a composition provided a root Node.
fun View.setComposeContent(
    views: Views = stage!!.views,
    content: @Composable () -> Unit
): Composition {
    val context = MonotonicClockImpl(views) + views.coroutineContext

    val snapshotManager = GlobalSnapshotManager(views.gameWindow.coroutineDispatcher)
    snapshotManager.ensureStarted()

    val recomposer = Recomposer(context)

    CoroutineScope(context).launch(start = CoroutineStart.UNDISPATCHED) {
        println("runRecomposeAndApplyChanges")
        recomposer.runRecomposeAndApplyChanges()
    }

    //CoroutineScope(context).launch(start = CoroutineStart.UNDISPATCHED) {
    //}

    return ControlledComposition(NodeApplier(this), recomposer).apply {
        setContent(content)
    }
}

class GlobalSnapshotManager(val dispatcher: CoroutineDispatcher) {
    private var started = false
    private var commitPending = false
    private var removeWriteObserver: (ObserverHandle)? = null

    private val scheduleScope = CoroutineScope(dispatcher + SupervisorJob())

    fun ensureStarted() {
        if (!started) {
            started = true
            removeWriteObserver = Snapshot.registerGlobalWriteObserver(globalWriteObserver)
        }
    }

    private val globalWriteObserver: (Any) -> Unit = {
        // Race, but we don't care too much if we end up with multiple calls scheduled.
        if (!commitPending) {
            commitPending = true
            schedule {
                commitPending = false
                Snapshot.sendApplyNotifications()
            }
        }
    }

    /**
     * List of deferred callbacks to run serially. Guarded by its own monitor lock.
     */
    private val scheduledCallbacks = mutableListOf<() -> Unit>()

    /**
     * Guarded by [scheduledCallbacks]'s monitor lock.
     */
    private var isSynchronizeScheduled = false

    /**
     * Synchronously executes any outstanding callbacks and brings snapshots into a
     * consistent, updated state.
     */
    private fun synchronize() {
        scheduledCallbacks.forEach { it.invoke() }
        scheduledCallbacks.clear()
        isSynchronizeScheduled = false
    }

    private fun schedule(block: () -> Unit) {
        scheduledCallbacks.add(block)
        if (!isSynchronizeScheduled) {
            isSynchronizeScheduled = true
            scheduleScope.launch { synchronize() }
        }
    }
}

class MonotonicClockImpl(val views: Views) : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (Long) -> R
    ): R {
        println("MonotonicClockImpl.withFrameNanos")
        return suspendCoroutine { continuation ->
            val start = DateTime.now()
            views.stage.onNextFrame {
                val now = DateTime.now()
                continuation.resume(onFrame((now - start).nanoseconds.toLong()))
            }
        }
    }
}

@Composable fun Text(text: String, onClick: () -> Unit = {}) {
    ComposeNode<UIText, NodeApplier>({
        UIText("DUMMY", height = UIButton.DEFAULT_HEIGHT).also {
            println("Created UIText")
        }
    }) {
        set(text) { this.text = it }
        set(onClick) { this.onClick { onClick() } }
    }
}

@Composable fun Button(text: String, onClick: () -> Unit = {}) {
    ComposeNode<UIButton, NodeApplier>({
        UIButton().also {
            println("Created UIButton")
        }
    }) {
        set(text) { this.text = it }
        set(onClick) { this.onClick { onClick() } }
    }
}

@Composable fun VStack(content: @Composable () -> Unit) {
    ComposeNode<UIVerticalStack, NodeApplier>(::UIVerticalStack, {}, content)
}

@Composable fun HStack(content: @Composable () -> Unit) {
    ComposeNode<UIHorizontalStack, NodeApplier>(::UIHorizontalStack, {}, content)
}

// TOOLS

fun Container.removeChildAt(index: Int): Boolean {
    return removeChild(getChildAtOrNull(index))
}

// @TODO: Optimize
fun Container.removeChildAt(index: Int, count: Int) {
    repeat(count) { removeChildAt(index) }
}

fun Container.swapChildrenAt(indexA: Int, indexB: Int) {
    val a = getChildAtOrNull(indexA) ?: return
    val b = getChildAtOrNull(indexB) ?: return
    swapChildren(a, b)
}