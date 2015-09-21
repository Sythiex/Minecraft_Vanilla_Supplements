package com.sythiex.vanilla_supplements.items;

import com.sythiex.vanilla_supplements.VanillaSupplements;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;

public class ItemEmeraldHoe extends ItemHoe
{
	public ItemEmeraldHoe(ToolMaterial p_i45343_1_)
	{
		super(p_i45343_1_);
		setUnlocalizedName("emeraldHoe");
		setTextureName(VanillaSupplements.MODID + ":emeraldHoe");
		setCreativeTab(CreativeTabs.tabTools);
	}
}