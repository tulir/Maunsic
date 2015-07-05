package net.maunium.Maunsic.Util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;

/**
 * Most of the code by godshawk from CheatingEssentials, so credit to him.
 * 
 * @author Tulir293
 * @author godshawk
 * @since 0.1
 * @from CheatingEssentials
 */
public class GLHelper {
	/**
	 * Draw a box around a BoundingBox
	 * 
	 * @param aabb The Bounding Box that specifies the area.
	 * @param r The red value of the color to draw the line in.
	 * @param g The green value of the color to draw the box in.
	 * @param b The blue value of the color to draw the box in.
	 */
	public static void drawBoundingBox(AxisAlignedBB aabb, double r, double g, double b) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, 0.1825F);
		GLHelper.drawBoundingBoxFilling(aabb);
		GL11.glColor4d(r, g, b, 1.0F);
		GLHelper.drawBoundingBoxOutline(aabb);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	/**
	 * Draw a line from location A to B.
	 * 
	 * @param x1 The X coordinate of location A.
	 * @param y1 The Y coordinate of location A.
	 * @param z1 The Z coordinate of location A.
	 * @param x2 The X coordinate of location B.
	 * @param y2 The Y coordinate of location B.
	 * @param z2 The Z coordinate of location B.
	 * @param width The width of the line
	 * @param r The red value of the color to draw the line in.
	 * @param g The green value of the color to draw the line in.
	 * @param b The blue value of the color to draw the line in.
	 * @param a The alpha value of the color to draw the line in.
	 */
	public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float width, double r, double g, double b, double a) {
		drawLine(x1, y1, z1, x1, y1, z1, x2, y2, z2, width, r, g, b, a);
	}
	
	public static void drawLine(double transx, double transy, double transz, double x1, double y1, double z1, double x2, double y2, double z2, float width, double r, double g, double b, double a) {
		GL11.glPushMatrix();
		GL11.glTranslated(-transx, -transy, -transz);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(width);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(r, g, b, a);
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		t.startDrawing(GL11.GL_LINES);
		t.addVertex(x1, y1, z1);
		t.addVertex(x2, y2, z2);
		Tessellator.getInstance().draw();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	/**
	 * Fills the sides of the bounding box.
	 * 
	 * @param aabb The bounding box that defines the sides.
	 */
	public static void drawBoundingBoxFilling(AxisAlignedBB aabb) {
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		// START ?????
		t.startDrawingQuads();
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		// START ?????
		t.startDrawingQuads();
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		// START TOP
		t.startDrawingQuads();
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		// START BOTTOM
		t.startDrawingQuads();
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		// START ?????
		t.startDrawingQuads();
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		// START ?????
		t.startDrawingQuads();
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
	}
	
	/**
	 * Draws lines for the edges of the bounding box.
	 * 
	 * @param aabb The bounding box that defines the edges.
	 */
	public static void drawBoundingBoxOutline(AxisAlignedBB aabb) {
		WorldRenderer t = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		t.startDrawing(3);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		t.startDrawing(3);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
		t.startDrawing(1);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.minZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.maxX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.minY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		t.addVertex(aabb.minX - rm.renderPosX, aabb.maxY - rm.renderPosY, aabb.maxZ - rm.renderPosZ);
		Tessellator.getInstance().draw();
	}
	
	public static int toInt(int red, int green, int blue, int alpha) {
		return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF) << 0;
	}
	
	public static int[] toRGBA(int color) {
		Color c = new Color(color);
		return new int[] { c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue() };
	}
}