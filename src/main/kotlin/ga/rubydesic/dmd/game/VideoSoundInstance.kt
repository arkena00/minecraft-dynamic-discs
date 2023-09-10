package ga.rubydesic.dmd.game

import ga.rubydesic.dmd.MOD_ID
import ga.rubydesic.dmd.config
import ga.rubydesic.dmd.download.MusicId
import net.minecraft.client.sound.Sound
import net.minecraft.client.sound.SoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.sound.WeightedSoundSet
import net.minecraft.client.util.math.Vector3d
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.math.floatprovider.ConstantFloatProvider
import net.minecraft.util.math.floatprovider.FloatSupplier

class VideoSoundInstance @JvmOverloads constructor(
    videoId: MusicId,
    private val position: Vector3d,
    private val isRelative: Boolean = false,
    private val attenuation: SoundInstance.AttenuationType = SoundInstance.AttenuationType.LINEAR,
    attenuationDistance: Int = config.attenuationDistance,
    private val volume: FloatSupplier = ConstantFloatProvider.create(1f),
    private val pitch: FloatSupplier = ConstantFloatProvider.create(1f),
) : SoundInstance {
    private val id = Identifier(MOD_ID, videoId.toString())
    override fun getId() = id

    private val weighed = WeightedSoundSet(id, null)
    private val sound =
        Sound(id.toString(), volume, pitch, 1, Sound.RegistrationType.SOUND_EVENT, true, false, attenuationDistance)

    override fun getSoundSet(soundManager: SoundManager?) = weighed

    override fun getSound() = sound
    override fun getCategory() = SoundCategory.RECORDS
    override fun isRepeatable() = false
    override fun isRelative() = isRelative
    override fun getRepeatDelay() = 0
    override fun getVolume() = 1f
    override fun getPitch() = 1f
    override fun getX() = position.x
    override fun getY() = position.y
    override fun getZ() = position.z
    override fun getAttenuationType() = attenuation
}