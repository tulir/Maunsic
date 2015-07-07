package net.maunium.Maunsic.Actions;

import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Actions.Util.TickAction;
import net.maunium.Maunsic.Util.GLHelper;
import net.maunium.Maunsic.Util.MaunsiConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Trajectories for bows.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class ActionTrajectories implements TickAction {
	private double x, y, z;
	private double motionX, motionY, motionZ;
	private double r, g, b;
	private boolean hasHitEntity = false, active = false;
	private Timer t;
	
	public ActionTrajectories() {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			Field f = Minecraft.class.getField("timer");
			f.setAccessible(true);
			t = (Timer) f.get(mc);
		} catch (Exception e) {
			Maunsic.getLogger().error("Failed to reflect Minecraft timer object to me.");
			Maunsic.getLogger().catching(e);
		}
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public String[] getStatusText() {
		return new String[] { "Trajectories " + EnumChatFormatting.GREEN + "ON" };
	}
	
	@Override
	@SuppressWarnings({ "null", "unchecked" })
	public void execute() {
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager rm = mc.getRenderManager();
		double dist = mc.thePlayer.getDistance(x, y, z);
		if (hasHitEntity) {
			r = 1.0;
			g = 0.05;
			b = 0.05;
		} else {
			r = dist / 100;
			g = 1.0;
			b = 0.0;
		}
		
		EntityPlayerSP p = mc.thePlayer;
		World w = mc.thePlayer.worldObj;
		if (p.getCurrentEquippedItem() != null) {
			Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
			if (item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl) {
				x = p.lastTickPosX + (p.posX - p.lastTickPosX) * t.renderPartialTicks - MathHelper.cos((float) Math.toRadians(p.rotationYaw)) * 0.16F;
				y = p.lastTickPosY + (p.posY - p.lastTickPosY) * t.renderPartialTicks + p.getEyeHeight() - 0.100149011612D;
				z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * t.renderPartialTicks - MathHelper.sin((float) Math.toRadians(p.rotationYaw)) * 0.16F;
				float con = !(item instanceof ItemBow) ? 0.4F : 1.0F;
				
				motionX = -MathHelper.sin((float) Math.toRadians(p.rotationYaw)) * MathHelper.cos((float) Math.toRadians(p.rotationPitch)) * con;
				motionZ = MathHelper.cos((float) Math.toRadians(p.rotationYaw)) * MathHelper.cos((float) Math.toRadians(p.rotationPitch)) * con;
				motionY = -MathHelper.sin((float) Math.toRadians(p.rotationPitch)) * con;
				double ssum = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
				
				motionX /= ssum;
				motionY /= ssum;
				motionZ /= ssum;
				
				if (item instanceof ItemBow) {
					float pow = (72000 - p.getItemInUseCount()) / 20.0F;
					pow = (pow * pow + pow * 2.0F) / 3.0F;
					
					if (pow > 1.0F) {
						pow = 1.0F;
					}
					
					if (pow <= 0.1F) {
						pow = 1.0F;
					}
					
					pow *= 2.0F;
					pow *= 1.5F;
					motionX *= pow;
					motionY *= pow;
					motionZ *= pow;
				} else {
					motionX *= 1.5D;
					motionY *= 1.5D;
					motionZ *= 1.5D;
				}
				
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glBlendFunc(770, 771);
				GL11.glEnable(3042);
				GL11.glDisable(3553);
				GL11.glDisable(2929);
				GL11.glEnable(GL13.GL_MULTISAMPLE);
				GL11.glDepthMask(false);
				GL11.glLineWidth(1.8F);
				GL11.glColor3d(r, g, b);
				GL11.glBegin(GL11.GL_LINE_STRIP);
//				boolean hasHitBlock = false;
				EntityLivingBase elb = null;
				double gravity = item instanceof ItemBow ? 0.05D : 0.03D;
				
				int sideHit = -1;
//				for (int q = 0; true; ++q) {
				while (true) {
					double rx = x * 1.0D - rm.renderPosX;
					double ry = y * 1.0D - rm.renderPosY;
					double rz = z * 1.0D - rm.renderPosZ;
					GL11.glVertex3d(rx, ry, rz);
					
					x += motionX;
					y += motionY;
					z += motionZ;
					motionX *= 0.99D;
					motionY *= 0.99D;
					motionZ *= 0.99D;
					motionY -= gravity;
					
					List<EntityLivingBase> es = w.getEntitiesWithinAABB(EntityLivingBase.class,
							AxisAlignedBB.fromBounds(x - 0.45, y - 0.45, z - 0.45, x + 0.45, y + 0.45, z + 0.45));
					if (!es.isEmpty()) {
						elb = es.get(0);
						if (elb.equals(p) && es.size() > 1) elb = es.get(1);
						if (!elb.equals(p)) {
							sideHit = -1;
							break;
						} else elb = null;
					}
					MovingObjectPosition mop = mc.theWorld.rayTraceBlocks(new Vec3(p.posX, p.posY + p.getEyeHeight(), p.posZ), new Vec3(x, y, z));
					if (mop != null) {
						if (mop.entityHit != null) System.out.println(mop.entityHit);
//						hasHitBlock = true;
						sideHit = mop.subHit;
						break;
					}
				}
				GL11.glEnd();
				
//				AxisAlignedBB bbox = AxisAlignedBB.fromBounds(x - 0.5 - rm.renderPosX, y - 0.5 - rm.renderPosY, z - 0.5 - rm.renderPosZ, x - 0.5
//						- rm.renderPosX + 1, y - 0.5 - rm.renderPosY + 1, z - 0.5 - rm.renderPosZ + 1);
				
				GL11.glTranslated(x - rm.renderPosX, y - rm.renderPosY, z - rm.renderPosZ);
				GL11.glRotatef(mc.thePlayer.rotationYaw, 0.0F, (float) (y - rm.renderPosY), 0.0F);
				GL11.glTranslated(-(x - rm.renderPosX), -(y - rm.renderPosY), -(z - rm.renderPosZ));
				drawESP(x - 0.35 - rm.renderPosX, y - 0.5 - rm.renderPosY, z - 0.5 - rm.renderPosZ, r, b, g);
				GL11.glDisable(3042);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDisable(GL13.GL_MULTISAMPLE);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glPopMatrix();
				
				hasHitEntity = elb != null;
				if (hasHitEntity) {
					float hW = elb.width / 2.0F;
					AxisAlignedBB bb = AxisAlignedBB.fromBounds(elb.posX - hW, elb.posY, elb.posZ - hW, elb.posX + hW, elb.posY + elb.height, elb.posZ + hW);
					GLHelper.drawBoundingBox(bb, r, g, b);
				} else drawSquare(x, y, z, 1F + (float) dist / 75.0F, sideHit, r, g, b, 255);
			}
		}
	}
	
	private void drawSquare(double x, double y, double z, float size, double side, double r, double g, double b, double a) {
		if (side < 0) return;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(size);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, a);
		
		if (side < 2) drawSquareUD(x - size, z - size, x + size, z + size, y);
		else if (side < 4) drawSquareEW(x - size, y - size, x + size, y + size, z);
		else if (side < 6) drawSquareNS(z - size, y - size, z + size, y + size, x);
		
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void drawSquareUD(double minX, double minZ, double maxX, double maxZ, double y) {
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		t.startDrawing(GL11.GL_LINES);
		t.addVertex(minX - rm.renderPosX, y - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, y - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, y - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, y - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, y - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, y - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, y - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, y - rm.renderPosY, minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
	}
	
	private void drawSquareEW(double minX, double minY, double maxX, double maxY, double z) {
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		t.startDrawing(GL11.GL_LINES);
		t.addVertex(minX - rm.renderPosX, minY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, minY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, maxY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, maxY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, minY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(minX - rm.renderPosX, maxY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, maxY - rm.renderPosY, z - rm.renderPosZ);
		t.addVertex(maxX - rm.renderPosX, minY - rm.renderPosY, z - rm.renderPosZ);
		Tessellator.getInstance().draw();
	}
	
	private void drawSquareNS(double minZ, double minY, double maxZ, double maxY, double x) {
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		t.startDrawing(GL11.GL_LINES);
		t.addVertex(x - rm.renderPosX, minY - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, minY - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, maxY - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, maxY - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, minY - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, maxY - rm.renderPosY, minZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, maxY - rm.renderPosY, maxZ - rm.renderPosZ);
		t.addVertex(x - rm.renderPosX, minY - rm.renderPosY, maxZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
	}
	
	private void drawESP(double d, double d1, double d2, double r, double b, double g) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, 0.1825F);
		GLHelper.drawBoundingBoxFilling(AxisAlignedBB.fromBounds(d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0));
		GL11.glColor4d(r, g, b, 1.0F);
		GLHelper.drawBoundingBoxOutline(AxisAlignedBB.fromBounds(d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0));
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
	
	@Override
	public void saveData(MaunsiConfig conf) {}
	
	@Override
	public void loadData(MaunsiConfig conf) {}
}
