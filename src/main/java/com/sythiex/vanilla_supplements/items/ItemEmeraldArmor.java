package com.sythiex.vanilla_supplements.items;

import com.sythiex.vanilla_supplements.VanillaSupplements;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemEmeraldArmor extends ItemArmor
{
	public ItemEmeraldArmor(ArmorMaterial material, int armorType)
	{
		super(material, 0, armorType);
		switch(armorType)
		{
		case 0:	setUnlocalizedName("emeraldHelmet");
				setTextureName(VanillaSupplements.MODID + ":emeraldHelmet");
				break;
		case 1:	setUnlocalizedName("emeraldChestplate");
				setTextureName(VanillaSupplements.MODID + ":emeraldChestplate");
				break;
		case 2:	setUnlocalizedName("emeraldLeggings");
				setTextureName(VanillaSupplements.MODID + ":emeraldLeggings");
				break;
		case 3:	setUnlocalizedName("emeraldBoots");
				setTextureName(VanillaSupplements.MODID + ":emeraldBoots");
				break;
		}
		setCreativeTab(CreativeTabs.tabCombat);
		canRepair = true;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack itemStack1, ItemStack itemStack2)
    {
        return Items.emerald == itemStack2.getItem() ? true : super.getIsRepairable(itemStack1, itemStack2);
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return VanillaSupplements.MODID + ":textures/armor/emeraldArmor_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
}