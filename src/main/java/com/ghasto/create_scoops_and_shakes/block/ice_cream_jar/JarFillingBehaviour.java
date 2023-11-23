package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.RegisteredObjects;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class JarFillingBehaviour extends BlockSpoutingBehaviour {
	public JarFillingBehaviour(){}
	@Override
	public long fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity == null) {
			return 0L;
		} else {
			ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(blockEntity.getType());
			if (!registryName.equals(CreateScoopsAndShakes.id("ice_cream_jar"))) {
				return 0L;
			} else {
				IceCreamJarBlockEntity jar = (IceCreamJarBlockEntity) blockEntity;
				Storage<FluidVariant> handler = jar.fluidStorage;
				if (handler == null) {
					return 0L;
				} else {
					long amount = availableFluid.getAmount();
					Transaction t = TransferUtil.getTransaction();

					long var15;
					label98: {
						long var21;
						try {
							label99: {
								Transaction nested;
								label106: {
									long inserted = handler.insert(availableFluid.getType(), amount, t);
									if (amount < 40500L) {
										nested = t.openNested();

										try {
											if (handler.insert(availableFluid.getType(), 1L, nested) == 1L) {
												var15 = 0L;
												break label106;
											}
										} catch (Throwable var19) {
											if (nested != null) {
												try {
													nested.close();
												} catch (Throwable var18) {
													var19.addSuppressed(var18);
												}
											}

											throw var19;
										}

										if (nested != null) {
											nested.close();
										}
									}

									if (!simulate) {
										t.commit();
									}

									var21 = inserted;
									break label99;
								}

								if (nested != null) {
									nested.close();
								}
								break label98;
							}
						} catch (Throwable var20) {
							if (t != null) {
								try {
									t.close();
								} catch (Throwable var17) {
									var20.addSuppressed(var17);
								}
							}

							throw var20;
						}

						if (t != null) {
							t.close();
						}

						return var21;
					}

					if (t != null) {
						t.close();
					}

					return var15;
				}
			}
		}
	}
	}
