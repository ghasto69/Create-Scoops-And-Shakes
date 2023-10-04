package com.ghasto.create_scoops_and_shakes.block.blender;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction;

public class BlenderInstance extends EncasedCogInstance implements DynamicInstance {

	private final RotatingData blenderHead;
	private final OrientedData blenderPole;
	private final BlenderBlockEntity blender;

	public BlenderInstance(MaterialManager materialManager, BlenderBlockEntity blockEntity) {
		super(materialManager, blockEntity, false);
		this.blender = blockEntity;

		blenderHead = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState)
				.createInstance();

		blenderHead.setRotationAxis(Direction.Axis.Y);

		blenderPole = getOrientedMaterial()
				.getModel(AllPartialModels.MECHANICAL_MIXER_POLE, blockState)
				.createInstance();


		float renderedHeadOffset = getRenderedHeadOffset();

		transformPole(renderedHeadOffset);
		transformHead(renderedHeadOffset);
	}

	@Override
	protected Instancer<RotatingData> getCogModel() {
		return materialManager.defaultSolid()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(AllPartialModels.MILLSTONE_COG, blockEntity.getBlockState());
	}

	@Override
	public void beginFrame() {

		float renderedHeadOffset = getRenderedHeadOffset();

		transformPole(renderedHeadOffset);
		transformHead(renderedHeadOffset);
	}

	private void transformHead(float renderedHeadOffset) {
		float speed = blender.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());

		blenderHead.setPosition(getInstancePosition())
				.nudge(0, -renderedHeadOffset, 0)
				.setRotationalSpeed(speed * 2);
	}

	private void transformPole(float renderedHeadOffset) {
		blenderPole.setPosition(getInstancePosition())
				.nudge(0, -renderedHeadOffset, 0);
	}

	private float getRenderedHeadOffset() {
		return blender.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks());
	}

	@Override
	public void updateLight() {
		super.updateLight();

		relight(pos.below(), blenderHead);
		relight(pos, blenderPole);
	}

	@Override
	public void remove() {
		super.remove();
		blenderHead.delete();
		blenderPole.delete();
	}
}
