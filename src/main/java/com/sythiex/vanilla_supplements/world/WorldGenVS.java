package com.sythiex.vanilla_supplements.world;

import java.util.Random;

import com.sythiex.vanilla_supplements.VanillaSupplements;
import com.sythiex.vanilla_supplements.blocks.BlockBushVS;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenVS implements IWorldGenerator
{
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(VanillaSupplements.addBerries)
			genBushes(rand, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	}
	
	/**
	 * Generates bushes
	 * @param rand
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @param chunkGenerator
	 * @param chunkProvider
	 */
	private void genBushes(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		int x = chunkX * 16 + rand.nextInt(16);
		int z = chunkZ * 16 + rand.nextInt(16);
		int y = getTopSolidBlock(world, x, z);
		
		if((world.getBlock(x, y, z) == Blocks.grass || world.getBlock(x, y, z) == Blocks.dirt) && rand.nextInt(30) == 1)
		{
			++y;
			
			if(isBlockReplacable(world, x, y, z))
			{
				//(x + i, y + j, z + k)
				for(int j = 0; j <= 1; j++)
				{
					for(int i = -1; i <= 1; i++)
					{
						for(int k = -1; k <= 1; k++)
						{
							if(isBlockReplacable(world, x + i, y + j, z + k))
							{
								if(i == 0 && j == 0 && k == 0)
								{
									world.setBlock(x + i, y + j, z + k, VanillaSupplements.berryBush, BlockBushVS.maxGrowthStage, 2);
								}
								else if(j == 0 && rand.nextInt(2) == 1 && isBlockReplacable(world, x + i, y + j, z + k) && VanillaSupplements.berryBush.canBlockStay(world, x + i, y + j, z + k))
								{
									world.setBlock(x + i, y + j, z + k, VanillaSupplements.berryBush, BlockBushVS.maxGrowthStage, 2);
								}
								else if(j == 1 && rand.nextInt(8) == 1 && isBlockReplacable(world, x + i, y + j, z + k) && VanillaSupplements.berryBush.canBlockStay(world, x + i, y + j, z + k))
								{
									world.setBlock(x + i, y + j, z + k, VanillaSupplements.berryBush, BlockBushVS.maxGrowthStage, 2);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * returns the y coordinate of the highest solid block at the given x and z coordinates
	 * @param world
	 * @param x
	 * @param z
	 * @return y
	 */
	private int getTopSolidBlock(World world, int x, int z)
	{
		int y = 256;
		
		while(!world.getBlock(x, y, z).getMaterial().blocksMovement() || (world.getBlock(x, y, z).getMaterial() == Material.leaves) || world.getBlock(x, y, z).isFoliage(world, x, y, z))
		{
			--y;
		}
		return y;
	}
	
	/**
	 * determines if the block is OK to replace during world feature generation (returns false for liquids)
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return true if replaceable
	 */
	private boolean isBlockReplacable(World world, int x, int y, int z)
	{
		if((!world.getBlock(x, y, z).getMaterial().blocksMovement() || world.getBlock(x, y, z).getMaterial() == Material.leaves || world.getBlock(x, y, z).isFoliage(world, x, y, z)) 
				&& !world.getBlock(x, y, z).getMaterial().isLiquid())
		{
			return true;
		}
		return false;
	}
}