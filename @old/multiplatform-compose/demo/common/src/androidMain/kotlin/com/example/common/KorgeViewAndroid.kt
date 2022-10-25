/*

@Composable
fun KorgeView(
    modifier: Modifier = Modifier,
    virtualWidth: Int = 512,
    virtualHeight: Int = 512,
    anchor: Anchor = Anchor.TOP_LEFT,
    scaleMode: ScaleMode = ScaleMode.SHOW_ALL,
    bgcolor: RGBA = com.soywiz.korim.color.Colors.DARKCYAN,
    block: suspend Stage.() -> Unit
) {
    class DefaultSceneParams(val callback: suspend Stage.() -> Unit, dummy: Unit = Unit)
    class DefaultScene(val params: DefaultSceneParams) : Scene() {
        override suspend fun Container.sceneInit() {
            params.callback(this.stage!!)
        }
    }
    AndroidView(
        factory = {
            KorgeAndroidView(it).also {
                it.loadModule(object : Module() {
                    override val mainScene: KClass<out Scene> get() = DefaultScene::class
                    override val scaleAnchor: Anchor = anchor
                    override val scaleMode: ScaleMode = scaleMode
                    override val size: SizeInt = SizeInt(virtualWidth, virtualHeight)
                    override val bgcolor: RGBA = bgcolor

                    override suspend fun AsyncInjector.configure() {
                        mapPrototype { DefaultScene(get()) }
                        mapInstance(DefaultSceneParams(block))
                    }
                })
            }
        },
        modifier = modifier,
        update = {
            //println("UPDATE")
        }
    )
}
 */