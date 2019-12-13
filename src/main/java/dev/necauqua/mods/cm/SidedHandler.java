package dev.necauqua.mods.cm;

import dev.necauqua.mods.cm.item.ItemRecalibrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

import static dev.necauqua.mods.cm.ChiseledMe.MODID;

public abstract class SidedHandler {

    @SidedProxy(modId = MODID)
    public static SidedHandler instance;

    public void registerDefaultModel(Item item) {}

    @Nullable
    public Entity getClientEntityById(int id) {
        return null;
    }


    public String getLocalization(String key, Object... format) {
        return "";
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused") // the above @SidedProxy annotation uses it
    public static final class ClientProxy extends SidedHandler {

        @Override
        public void registerDefaultModel(Item item) {
            ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
            ModelResourceLocation mrl = new ModelResourceLocation(registryName, "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, mrl);

            // I really hope that in next versions the advancement icons would support NBT,
            // so leaving this as a dirty hack (see also property overrides in ItemRecalibrator)
            if (item instanceof ItemRecalibrator) {
                for (int i = 1; i <= 16; i++) {
                    ModelLoader.setCustomModelResourceLocation(item, i, mrl);
                }
            }
        }

        @Nullable
        @Override
        public Entity getClientEntityById(int id) {
            Minecraft mc = Minecraft.getMinecraft();
            if (id == -1) {
                return mc.player;
            }
            if (mc.world == null) {
                return null;
            }
            return mc.world.getEntityByID(id);
        }

        @Override
        public String getLocalization(String key, Object... parameters) {
            return I18n.format(key, parameters);
        }
    }

    @SideOnly(Side.SERVER)
    @SuppressWarnings("unused") // the above @SidedProxy annotation uses it
    public static final class ServerProxy extends SidedHandler {}
}