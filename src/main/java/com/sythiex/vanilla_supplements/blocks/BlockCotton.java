package com.sythiex.vanilla_supplements.blocks;

import java.util.ArrayList;
import java.util.Random;

import com.sythiex.vanilla_supplements.VanillaSupplements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockCotton extends BlockCropVS
{
	public BlockCotton()
	{
		setBlockName("blockCotton");
		setBlockTextureName(VanillaSupplements.MODID + ":cotton_stage_0");
	}
	
	@Override
	public ArrayList <ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList <ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaSupplements.seedCotton));
		
		if(metadata >= maxGrowthStage)
		{
			drops.add(new ItemStack(VanillaSupplements.cotton, world.rand.nextInt(3) + 1));
		}
		
		return drops;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
		return new ItemStack(VanillaSupplements.seedCotton);
    }
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		iIcon = new IIcon[maxGrowthStage + 1];
		iIcon[0] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_0");
		iIcon[1] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_1");
		iIcon[2] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_2");
		iIcon[3] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_3");
		iIcon[4] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_4");
		iIcon[5] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_5");
		iIcon[6] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_6");
		iIcon[7] = iconRegister.registerIcon(VanillaSupplements.MODID + ":cotton_stage_7");
	}
}