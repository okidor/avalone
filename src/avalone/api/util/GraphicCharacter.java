package avalone.api.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

public enum GraphicCharacter
{
	_a(0,26),
	_b(14,26),
	_c(28,26),
	_d(42,26),
	_e(56,26),
	_f(70,26),
	_g(84,26),
	_h(98,26),
	_i(112,26),
	_j(126,26),
	_k(140,26),
	_l(154,26),
	_m(168,26),
	_n(182,26),
	_o(196,26),
	_p(210,26),
	_q(224,26),
	_r(238,26),
	_s(252,26),
	_t(266,26),
	_u(280,26),
	_v(294,26),
	_w(308,26),
	_x(322,26),
	_y(336,26),
	_z(350,26),
	_A(0,0),
	_B(14,0),
	_C(28,0),
	_D(42,0),
	_E(56,0),
	_F(70,0),
	_G(84,0),
	_H(98,0),
	_I(112,0),
	_J(126,0),
	_K(140,0),
	_L(154,0),
	_M(168,0),
	_N(182,0),
	_O(196,0),
	_P(210,0),
	_Q(224,0),
	_R(238,0),
	_S(252,0),
	_T(266,0),
	_U(280,0),
	_V(294,0),
	_W(308,0),
	_X(322,0),
	_Y(336,0),
	_Z(350,0),
	_0(729,26),
	_1(743,26),
	_2(757,26),
	_3(771,26),
	_4(785,26),
	_5(799,26),
	_6(813,26),
	_7(827,26),
	_8(841,26),
	_9(855,26),
	_TIRET(715,26),
	_SPACE(710,0),
	_SLASH(729,0),
	_DPoint(883,26);
	
	/*private float height;
	private float length;*/
	private FPoint offset;
	private FPoint endOffset;
	private static final float imgHeight = 52.0000f;
	private static final float imgWidth = 1007.0000f;
	public static final int height = 26;
	public static final int width = 14;
	public static final float rapport = 14.0f/26.0f;
	
	private GraphicCharacter(float pixelOffsetX,float pixelOffsetY)
	{
		offset = new FPoint(pixelOffsetX/imgWidth,pixelOffsetY/imgHeight);
		endOffset = offset.clone(width/imgWidth,height/imgHeight);
	}
	
	public static GraphicCharacter getFromChar(char ch)
	{
		if(ch == ' ' || ch == '_')
		{
			return GraphicCharacter._SPACE;
		}
		else if(ch == '-')
		{
			return GraphicCharacter._TIRET;
		}
		else if(ch == '/')
		{
			return GraphicCharacter._SLASH;
		}
		else if(ch == ':')
		{
			return GraphicCharacter._DPoint;
		}
		//System.out.println(ch);
		return GraphicCharacter.valueOf("_" + ch);
	}
	
	public void drawCoordRect(Point p1,Point p2)
	{
		glBegin(GL_QUADS);
    	glTexCoord2f(offset.x, offset.y);
    	glVertex2i(p1.x, p1.y);
    	glTexCoord2f(offset.x, endOffset.y);
    	glVertex2i(p1.x, p2.y);
    	glTexCoord2f(endOffset.x, endOffset.y);
    	glVertex2i(p2.x, p2.y);
    	glTexCoord2f(endOffset.x, offset.y);
    	glVertex2i(p2.x, p1.y);
    	glEnd();
	}
}
