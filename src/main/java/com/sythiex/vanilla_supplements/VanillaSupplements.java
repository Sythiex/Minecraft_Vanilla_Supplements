package com.sythiex.vanilla_supplements;

import com.sythiex.vanilla_supplements.blocks.BlockBerryBush;
import com.sythiex.vanilla_supplements.blocks.BlockCotton;
import com.sythiex.vanilla_supplements.items.ItemEmeraldArmor;
import com.sythiex.vanilla_supplements.items.ItemEmeraldAxe;
import com.sythiex.vanilla_supplements.items.ItemEmeraldHoe;
import com.sythiex.vanilla_supplements.items.ItemEmeraldPickaxe;
import com.sythiex.vanilla_supplements.items.ItemEmeraldShovel;
import com.sythiex.vanilla_supplements.items.ItemEmeraldSword;
import com.sythiex.vanilla_supplements.items.ItemFoodVS;
import com.sythiex.vanilla_supplements.items.ItemSeedsVS;
import com.sythiex.vanilla_supplements.items.ItemVS;
import com.sythiex.vanilla_supplements.items.ItemWand;
import com.sythiex.vanilla_supplements.network.CommonProxyVS;
import com.sythiex.vanilla_supplements.network.MessageHandlerVS;
import com.sythiex.vanilla_supplements.network.MessageVS;
import com.sythiex.vanilla_supplements.world.WorldGenVS;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = VanillaSupplements.MODID, version = VanillaSupplements.VERSION)

public class VanillaSupplements
{
	@SidedProxy(clientSide="com.sythiex.vanilla_supplements.network.ClientProxyVS", serverSide="com.sythiex.vanilla_supplements.network.ServerProxyVS")
	public static CommonProxyVS proxy;
	
    public static final String MODID = "vanilla_supplements";
    public static final String VERSION = "0.3.2";
    
    public static SimpleNetworkWrapper network;
    
    //config variables
    public static boolean addEmeraldTools;
    public static boolean addCotton;
    public static boolean addBerries;
    public static boolean addFertilizer;
    public static boolean addGrassBlockRecipe;
    public static boolean addClayRecipies;
    public static boolean addVineRecipe;
    public static boolean addStringRecipe;
    public static boolean addMossStoneRecipe;
    public static int bushSpawnRate;
    public static boolean enableEasterEgg;
    
    //blocks
    public static Block cottonPlant;
    public static Block berryBush;
    
    //items
    public static Item seedCotton;
    public static Item cotton;
    public static Item berry;
    public static Item fertilizer;
    
    public static Item wand;
    
    public static Item emeraldShovel;
    public static Item emeraldPickaxe;
    public static Item emeraldAxe;
    public static Item emeraldHoe;
    public static Item emeraldSword;
    
    public static Item emeraldHelmet;
    public static Item emeraldChestplate;
    public static Item emeraldLeggings;
    public static Item emeraldBoots;
    
    //materials
    static ToolMaterial trueEmerald_Tool = EnumHelper.addToolMaterial("trueEmerald_Tool", 3, 500, 6.0F, 2.0F, 25);
    static ArmorMaterial trueEmerald_Armor = EnumHelper.addArmorMaterial("trueEmerald_Armor", 23, new int[]{3, 8, 6, 3}, 25);
    
    public static WorldGenVS worldGen = new WorldGenVS();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
        network.registerMessage(MessageHandlerVS.class, MessageVS.class, 0, Side.SERVER);
    	
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
    	addEmeraldTools = config.getBoolean("Enable emerald tools/armor", config.CATEGORY_GENERAL, true, "Only removes the recipies, can still get through creative mode");
    	addCotton = config.getBoolean("Enable cotton plants", config.CATEGORY_GENERAL, true, "");
    	addBerries = config.getBoolean("Enable berry bush generation", config.CATEGORY_GENERAL, true, "");
    	addFertilizer = config.getBoolean("Enable fertilizer from rotten flesh", config.CATEGORY_GENERAL, true, "");
    	addStringRecipe = config.getBoolean("Enable string recipe", config.CATEGORY_GENERAL, false, "Change to true to allow crafting string from wool (Considered overpowered with some mods)");
    	addGrassBlockRecipe = config.getBoolean("Enable grass block recipe", config.CATEGORY_GENERAL, true, "");
    	addClayRecipies = config.getBoolean("Enable clay convenience recipies", config.CATEGORY_GENERAL, true, "");
    	addVineRecipe = config.getBoolean("Enable vine recipe", config.CATEGORY_GENERAL, true, "");
    	addMossStoneRecipe = config.getBoolean("Enable 1.8 mossy cobblestone recipe", config.CATEGORY_GENERAL, true, "");
    	bushSpawnRate = config.getInt("Bush spawn rate", config.CATEGORY_GENERAL, 15, 2, 1000, "Lower values mean higher spawn rate");
    	enableEasterEgg = config.getBoolean("Enable Easter Egg", config.CATEGORY_GENERAL, true, "");
    	
    	config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    	registerBlocks();
    	registerItems();
    	registerOreDictionary();
    	registerRecipies();
    	
    	GameRegistry.registerWorldGenerator(worldGen, 1000);
    	
    	FMLCommonHandler.instance().bus().register(new EventHandlerVS());
    	
    	if(addCotton)
    		MinecraftForge.addGrassSeed(new ItemStack(seedCotton), 7);
    }
    
    private void registerBlocks()
    {
    	cottonPlant = new BlockCotton();
    	GameRegistry.registerBlock(cottonPlant, MODID + "_cottonPlant");
    	
    	berryBush = new BlockBerryBush();
    	GameRegistry.registerBlock(berryBush, MODID + "_berryBush");
    }
    
    private void registerItems()
    {
    	seedCotton = new ItemSeedsVS(cottonPlant, Blocks.farmland).setUnlocalizedName("seedCotton").setTextureName(MODID + ":seedCotton");
    	GameRegistry.registerItem(seedCotton, MODID + "_seedCotton");
    	
    	cotton = new ItemVS().setUnlocalizedName("cotton").setTextureName(MODID + ":cotton").setCreativeTab(CreativeTabs.tabMaterials);
    	GameRegistry.registerItem(cotton, MODID + "_cotton");
    	
    	berry = new ItemFoodVS(2, 1.6F).setUnlocalizedName("berry").setTextureName(MODID + ":berry").setCreativeTab(CreativeTabs.tabFood);
    	GameRegistry.registerItem(berry, MODID + "_berry");
    	
    	fertilizer = new ItemVS().setUnlocalizedName("fertilizer").setTextureName(MODID + ":fertilizer").setCreativeTab(CreativeTabs.tabMaterials);
    	GameRegistry.registerItem(fertilizer, MODID + "_fertilizer");
    	
    	wand = new ItemWand().setUnlocalizedName("wand").setTextureName(MODID + ":wand").setCreativeTab(CreativeTabs.tabTools);
    	GameRegistry.registerItem(wand, MODID + "_wand");
    	
    	//tools
    	emeraldShovel = new ItemEmeraldShovel(trueEmerald_Tool);
    	GameRegistry.registerItem(emeraldShovel, MODID + "_emeraldShovel");
    	
    	emeraldPickaxe = new ItemEmeraldPickaxe(trueEmerald_Tool);
    	GameRegistry.registerItem(emeraldPickaxe, MODID + "_emeraldPickaxe");
    	
    	emeraldAxe = new ItemEmeraldAxe(trueEmerald_Tool);
    	GameRegistry.registerItem(emeraldAxe, MODID + "_emeraldAxe");
    	
    	emeraldHoe = new ItemEmeraldHoe(trueEmerald_Tool);
    	GameRegistry.registerItem(emeraldHoe, MODID + "_emeraldHoe");
    	
    	emeraldSword = new ItemEmeraldSword(trueEmerald_Tool);
    	GameRegistry.registerItem(emeraldSword, MODID + "_emeraldSword");
    	
    	//armor
    	emeraldHelmet = new ItemEmeraldArmor(trueEmerald_Armor, 0);
    	GameRegistry.registerItem(emeraldHelmet, MODID + "_emeraldHelmet");
    		
    	emeraldChestplate = new ItemEmeraldArmor(trueEmerald_Armor, 1);
    	GameRegistry.registerItem(emeraldChestplate, MODID + "_emeraldChestplate");
    		
    	emeraldLeggings = new ItemEmeraldArmor(trueEmerald_Armor, 2);
    	GameRegistry.registerItem(emeraldLeggings, MODID + "_emeraldLeggings");
    		
    	emeraldBoots = new ItemEmeraldArmor(trueEmerald_Armor, 3);
    	GameRegistry.registerItem(emeraldBoots, MODID + "_emeraldBoots");
    }
    
    private void registerOreDictionary()
    {
    	//vanilla registries
    	OreDictionary.registerOre("dirt", new ItemStack(Blocks.dirt));
    	OreDictionary.registerOre("seedWheat", new ItemStack(Items.wheat_seeds));
    	OreDictionary.registerOre("listAllSeed", new ItemStack(Items.wheat_seeds));
    	OreDictionary.registerOre("listAllWater", new ItemStack(Items.water_bucket));
    	OreDictionary.registerOre("blockCloth", new ItemStack(Blocks.wool));
    	OreDictionary.registerOre("sand", new ItemStack(Blocks.sand));
    	
    	//mod registries
    	OreDictionary.registerOre("seedCotton", new ItemStack(seedCotton));
    	OreDictionary.registerOre("cropCotton", new ItemStack(cotton));
    	OreDictionary.registerOre("foodBerry", new ItemStack(berry));
    	OreDictionary.registerOre("cropBerry", new ItemStack(berry));
    	OreDictionary.registerOre("fertilizer", new ItemStack(fertilizer));
    	OreDictionary.registerOre("fertilizerOrganic", new ItemStack(fertilizer));
    }
    
    private void registerRecipies()
    {
    	if(addEmeraldTools)
    	{
    		//tools
    		/*GameRegistry.addRecipe(new ItemStack(emeraldShovel), new Object[]{
    			"E",
    			"S",
    			"S",
    			'E', Items.emerald, 'S', Items.stick});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldShovel, new Object[]{
    				"E",
    				"S",
    				"S",
    				'E', "gemEmerald", 'S', "stickWood"}));
    	
    		/*GameRegistry.addRecipe(new ItemStack(emeraldPickaxe), new Object[]{
    			"EEE",
    			" S ",
    			" S ",
    			'E', Items.emerald, 'S', Items.stick});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldPickaxe, new Object[]{
    				"EEE",
    				" S ",
    				" S ",
    				'E', "gemEmerald", 'S', "stickWood"}));
    	
    		/*GameRegistry.addRecipe(new ItemStack(emeraldAxe), new Object[]{
    			"EE",
    			"ES",
    			" S",
    			'E', Items.emerald, 'S', Items.stick});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldAxe, new Object[]{
    				"EE",
    				"ES",
    				" S",
    				'E', "gemEmerald", 'S', "stickWood"}));
    	
    		/*GameRegistry.addRecipe(new ItemStack(emeraldHoe), new Object[]{
    			"EE",
    			" S",
    			" S",
    			'E', Items.emerald, 'S', Items.stick});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldHoe, new Object[]{
    				"EE",
    				" S",
    				" S",
    				'E', "gemEmerald", 'S', "stickWood"}));
    	
    		/*GameRegistry.addRecipe(new ItemStack(emeraldSword), new Object[]{
    			"E",
    			"E",
    			"S",
    			'E', Items.emerald, 'S', Items.stick});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldSword, new Object[]{
    				"E",
    				"E",
    				"S",
    				'E', "gemEmerald", 'S', "stickWood"}));
    		
    		//armor
    		/*GameRegistry.addRecipe(new ItemStack(emeraldHelmet), new Object[]{
    			"EEE",
    			"E E",
    			'E', Items.emerald});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldHelmet, new Object[]{
    				"EEE",
    				"E E",
    				'E', "gemEmerald"}));
    		
    		/*GameRegistry.addRecipe(new ItemStack(emeraldChestplate), new Object[]{
    			"E E",
    			"EEE",
    			"EEE",
    			'E', Items.emerald});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldChestplate, new Object[]{
    				"E E",
    				"EEE",
    				"EEE",
    				'E', "gemEmerald"}));
    		
    		/*GameRegistry.addRecipe(new ItemStack(emeraldLeggings), new Object[]{
    			"EEE",
    			"E E",
    			"E E",
    			'E', Items.emerald});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldLeggings, new Object[]{
    				"EEE",
    				"E E",
    				"E E",
    				'E', "gemEmerald"}));
    		
    		/*GameRegistry.addRecipe(new ItemStack(emeraldBoots), new Object[]{
    			"E E",
    			"E E",
    			'E', Items.emerald});*/
    		
    		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldBoots, new Object[]{
    				"E E",
    				"E E",
    				'E', "gemEmerald"}));
    	}
    	
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.string, 3), new Object[]{
			"CCC",
			'C', "cropCotton"}));
    	
    	if(addFertilizer)
    	{
    		GameRegistry.addShapelessRecipe(new ItemStack(fertilizer), new ItemStack(Items.rotten_flesh));
    	}
    	
    	if(addGrassBlockRecipe)
    	{
    		//GameRegistry.addShapelessRecipe(new ItemStack(Blocks.grass), new ItemStack(Blocks.dirt), new ItemStack(Items.wheat_seeds));
    		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.grass), new Object[]{"dirt", "listAllSeed"}));
    	}
    	
    	if(addClayRecipies)
    	{
    		GameRegistry.addShapelessRecipe(new ItemStack(Items.clay_ball, 4), new ItemStack(Blocks.clay));
    		//GameRegistry.addShapelessRecipe(new ItemStack(Blocks.clay), new ItemStack(Blocks.dirt), new ItemStack(Blocks.dirt), new ItemStack(Blocks.sand), new ItemStack(Items.water_bucket));
    		
    		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.clay), new Object[]{"dirt", "dirt", "sand", "listAllWater"}));
    	}
    	
    	if(addVineRecipe)
    	{
    		GameRegistry.addRecipe(new ItemStack(Blocks.vine), new Object[]{
    			"GG",
    			"GG",
    			'G', Blocks.tallgrass});
    	}
    	
    	if(addStringRecipe)
    	{
    		//GameRegistry.addShapelessRecipe(new ItemStack(Items.string, 4), new ItemStack(Blocks.wool));
    		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.string, 4), new Object[]{"blockCloth"}));
    	}
    	
    	if(addMossStoneRecipe)
    	{
    		//GameRegistry.addShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.vine));
    		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.mossy_cobblestone), new Object[]{"cobblestone", new ItemStack(Blocks.vine)}));
    	}
    }
}