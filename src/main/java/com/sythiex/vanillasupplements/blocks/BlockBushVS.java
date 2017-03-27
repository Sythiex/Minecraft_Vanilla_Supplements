package com.sythiex.vanillasupplements.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBushVS extends BlockBush implements IGrowable
{
	public static int maxGrowthStage = 7;
	private int renderPass = 0;
	
	@SideOnly(Side.CLIENT)
    protected IIcon[] iIcon;
	
	public BlockBushVS()
	{
		super(Material.leaves);
		setTickRandomly(true);
		setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(0.6F);
        setLightOpacity(1);
        setStepSound(soundTypeGrass);
        disableStats();
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block block)
	{
		if(block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == this)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y - 1, z);
		
		if(block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this) || block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == this)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
	    super.updateTick(world, x, y, z, rand);
	    
	    if(world.getBlockLightValue(x, y, z) >= 9 && rand.nextInt(3) == 1)
	    {
	    	int growStage = world.getBlockMetadata(x, y, z) + 1;
	    	if (growStage > maxGrowthStage)
	    	{
	    		growStage = maxGrowthStage;
	    	}
	    	world.setBlockMetadataWithNotify(x, y, z, growStage, 2);
	    }
	}
	
	@Override
	public Item getItemDropped(int i, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}
	
	@Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
		return EnumPlantType.Plains;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return AxisAlignedBB.getBoundingBox((double)p_149668_2_ + this.minX, (double)p_149668_3_ + this.minY, (double)p_149668_4_ + this.minZ, (double)p_149668_2_ + this.maxX, (double)p_149668_3_ + this.maxY, (double)p_149668_4_ + this.maxZ);
    }
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return true;
    }
	
	@Override
	public int getRenderType()
	{
		return 0;
	}
	
	@Override
	public IIcon getIcon(int side, int growthStage)
	{
		return iIcon[growthStage];
	}
	
	/**
	 * is bonemeal allowed?
	 */
	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_)
	{
		return false;
	}

	/**
	 * are conditions for bonemeal growth tick acceptable?
	 */
	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_)
	{
		return false;
	}

	/**
	 * processes growth-tick
	 */
	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z){}
}