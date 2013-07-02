package organicchem.core.chem;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

public class Graph {
	private static final int LINE = 1;
	private static final int WEDGE = 2;
	private static final int ATOM = 3;
	private static final int BOUNDS = 4;
	private static final int RESOLUTION = 5;
	
	public int resolution;
	public float graphWidth, graphHeight;
	public ArrayList<Line> lines = new ArrayList();
	public ArrayList<Wedge> wedges = new ArrayList();
	public ArrayList<Atom> atoms = new ArrayList();
	
	public Graph(int[] data) {
		readData(data);
	}
	
	private void readData(int[] data) {
		int i = 0, charLen, j;
		char[] str;
		Line line;
		Wedge wedge;
		Atom atom;
		while (i < data.length) {
			switch(data[i++]) {
			case LINE:
				line = new Line();
				line.width = data[i++];
				line.dashed = data[i++];
				i++;//atom1
				i++;//atom2
				line.x1 = data[i++];
				line.y1 = data[i++];
				line.x2 = data[i++];
				line.y2 = data[i++];
				lines.add(line);
				break;
			case WEDGE:
				wedge = new Wedge();
				wedge.dashed = data[i++];
				i++;//atom1
				i++;//atom2
				wedge.x1 = data[i++];
				wedge.y1 = data[i++];
				wedge.x2 = data[i++];
				wedge.y2 = data[i++];
				wedge.x3 = data[i++];
				wedge.y3 = data[i++];
				wedges.add(wedge);
				break;
			case ATOM:
				atom = new Atom();
				i++;//id
				atom.x = data[i++];
				atom.y = data[i++];
				charLen = data[i++];
				str = new char[charLen];
				for (j = 0; j < charLen; ++j) {
					str[j] = (char) data[i++];
				}
				atom.text = String.valueOf(str);
				atom.orient = data[i++];
				atom.splitText();
				atoms.add(atom);
				break;
			case BOUNDS:
				if (data[i++] != 0) //x
					throw new RuntimeException();
				if (data[i++] != 0) //y
					throw new RuntimeException();
				this.graphWidth = data[i++];
				this.graphHeight = data[i++];
				break;
			case RESOLUTION:
				this.resolution = data[i++];
				break;
			default:
				throw new RuntimeException();
			}
		}
	}
	
	//x, y: used to render text
	public void render(float x, float y, float w, float h, FontRenderer fontRenderer) {
		
		
		//set transform
		float gw = graphWidth, gh = graphHeight;
		float rate;
		float trX, trY;
		if (gw / gh > w / h) {
			rate = w / gw;
			gh = gh * rate;
			trX = 0.0f;
			trY = (h - gh) / 2.0f;
		} else {
			rate = h / gh;
			gw = gw * rate;
			trX = (w - gw) / 2.0f;
			trY = 0.0f;
		}
		GL11.glTranslatef(trX, trY, 0.0f);
		GL11.glScalef(rate, rate, 1.0f);
		
		//draw lines
		for (Line line : lines) {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(line.x1, line.y1);
			GL11.glVertex2f(line.x2, line.y2);
			GL11.glEnd();
		}
		float halfW, halfH, xOffset;
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		
		//draw atoms
		float textRate = rate < 0.06f ? 0.5f : 1.0f;
		
		float textWidth, textHeight;
		textHeight = fontRenderer.FONT_HEIGHT * textRate;
		halfH = textHeight / 2;
		//GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glColor4f(139.0f / 255.0f, 139.0f / 255.0f, 139.0f / 255.0f, 1.0f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for (Atom atom : atoms) {
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			atom.drawX = atom.x * rate + trX + x;
			atom.drawY = atom.y * rate + trY + y;
			
			xOffset = getTextXOffset(atom, fontRenderer, textRate);
			textWidth = getTextWidth(atom, fontRenderer, textRate);
			
			atom.drawX -= xOffset;
			atom.drawY -= halfH;
			GL11.glTranslatef(atom.drawX, atom.drawY, 0.0f);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0.0f, 0.0f);
			GL11.glVertex2f(0.0f, textHeight);
			GL11.glVertex2f(textWidth, textHeight);
			GL11.glVertex2f(textWidth, 0.0f);
			GL11.glEnd();
			
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		for (Atom atom : atoms) {
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			renderText(atom, fontRenderer, textRate);
		}
		GL11.glPopMatrix();
		
	}
	
	private float getTextWidth(Atom atom, FontRenderer fontRenderer, float rate) {
		int i = -1;
		float r = 0.0f;
		while (++i < atom.renderText.length) {
			r += getTextWidth(atom.renderText[i], fontRenderer, rate);
		}
		return r;
	}
	
	private float getTextXOffset(Atom atom, FontRenderer fontRenderer, float rate) {
		if (atom.textCenter == 0) {
			return fontRenderer.getStringWidth(atom.renderText[0]) * rate / 2.0f;
		} else {
			return getTextWidth(atom, fontRenderer, rate)
					- getTextWidth(atom.renderText[atom.textCenter], fontRenderer, rate) / 2.0f;
		}
	}
	
	private static final float subscriptRate = 0.75f;
	private static final float subscriptOffsetY = 0.8f;
	
	private float getTextWidth(String str, FontRenderer fontRenderer, float rate) {
		char ch = str.charAt(0);
		if (ch >= '0' && ch <= '9') {
			return fontRenderer.getStringWidth(str) * rate * subscriptRate;
		}
		return fontRenderer.getStringWidth(str) * rate;
	}
	
	private void renderText(Atom atom, FontRenderer fontRenderer, float rate) {
		GL11.glTranslatef(atom.drawX, atom.drawY, 0.0f);
		GL11.glScalef(rate, rate, 1.0f);
		int i = -1;
		char ch;
		while (++i < atom.renderText.length) {
			ch = atom.renderText[i].charAt(0);
			if (ch >= '0' && ch <= '9') {
				GL11.glScalef(subscriptRate, subscriptRate, 1.0f);
				fontRenderer.drawString(atom.renderText[i], 0, (int)(fontRenderer.FONT_HEIGHT * subscriptOffsetY), -16777216);
				GL11.glScalef(1.0f / subscriptRate, 1.0f / subscriptRate, 1.0f);
			} else {
				fontRenderer.drawString(atom.renderText[i], 0, 0, -16777216);
			}
			GL11.glTranslatef(getTextWidth(atom.renderText[i], fontRenderer, rate) / rate, 0.0f, 0.0f);//TODO * or / rate
		}
	}
	
	public static class Line {
		int width;
		int dashed;
		float x1, y1, x2, y2;
	}
	public static class Wedge {
		int dashed;
		float x1, y1, x2, y2, x3, y3;
	}
	public static class Atom {
		float x, y;
		String text;
		int orient;
		float drawX, drawY;
		float w, h;
		
		String renderText[];
		int textCenter;	//0:renderText[0], n:renderText[n](n = renderText.length - 1)
		public void splitText() {
			ArrayList<String> strList = new ArrayList();
			char ch;
			
			boolean rightToLeft;
			rightToLeft = orient == 4;
			
			int criticalPos;//after the first or last element, whether is first or last depends on (text.charAt(0) == 'H')
			if (rightToLeft) {
				int i = text.length();
				while (--i >= 0) {
					ch = text.charAt(i);
					if (ch >= 'A' && ch <= 'Z')
						break;
				}
				criticalPos = i;
			} else {
				criticalPos = 1;
			}
			
			
			int i = 0, last = 0;
			int lastType = 0;	//0:char, 1:num
			int type = 0;
			while (++i < text.length()) {
				ch = text.charAt(i) ;
				type = (ch >= '0' && ch <= '9') ? 1 : 0;
				if (type != lastType || i == criticalPos) {
					strList.add(text.substring(last, i));
					lastType = type;
					last = i;
				}
			}
			strList.add(text.substring(last, i));
			renderText = strList.toArray(new String[strList.size()]);
			textCenter = rightToLeft ? renderText.length - 1 : 0;
		}
	}
}
