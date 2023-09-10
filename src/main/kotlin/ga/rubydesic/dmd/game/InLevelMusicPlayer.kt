package ga.rubydesic.dmd.game

import ga.rubydesic.dmd.download.MusicCache
import ga.rubydesic.dmd.download.MusicId
import ga.rubydesic.dmd.mixin.client.WorldRendererAccess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.sound.SoundInstance
import net.minecraft.client.util.math.Vector3d
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

fun WorldRenderer.playYoutubeMusic(id: MusicId, blockPos: BlockPos) {
    val minecraft = MinecraftClient.getInstance()
    val lra = this as WorldRendererAccess

    val currentlyPlaying: SoundInstance? = lra.playingSongs[blockPos]
    if (currentlyPlaying != null) {
        minecraft.soundManager.stop(currentlyPlaying)
        lra.playingSongs.remove(blockPos)
    }

    GlobalScope.launch(minecraft.asCoroutineDispatcher()) {
        val info = MusicCache.getPlaybackInfo(id)
        minecraft.inGameHud.setRecordPlayingOverlay(Text.of(info?.details?.title))
    }

    val nowPlaying: SoundInstance = VideoSoundInstance(
        id,
        Vector3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
    )
    minecraft.soundManager.play(nowPlaying)
    lra.playingSongs[blockPos] = nowPlaying

    //log.info("play sound ${minecraft.soundManager.isPlaying(nowPlaying)} ${nowPlaying.volume} ${nowPlaying.x} ${nowPlaying.y} ${nowPlaying.z}")
    //clientWorld.playSound(0.0, 0.0, 0.0, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f, false)

    this.callUpdateEntitiesForSong(clientWorld, blockPos, true)
}