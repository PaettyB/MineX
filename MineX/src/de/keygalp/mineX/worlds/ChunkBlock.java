package de.keygalp.mineX.worlds;

public class ChunkBlock {

	
	//public Map<Integer, float[]> faces = new HashMap<Integer, float[]>();
	
	public static byte[] frontFace = new byte[]{
	    /*0, 0, 1,
	    1, 0, 1,
	    1, 1, 1,
	    0, 1, 1,*/
		0,0,0,
		0,1,0,
		1,1,0,
		1,0,0,
		        
	};

	public static byte[] backFace  = new byte[]{
	   /* 1, 0, 0,
	    0, 0, 0,
	    0, 1, 0,
	    1, 1, 0,*/
		1,0,1,
		1,1,1,
		0,1,1,
		0,0,1,
	};

	public static byte[] leftFace  = new byte[]{
			/* 0, 0, 0,
	    0, 0, 1,
	    0, 1, 1,
	    0, 1, 0,*/
		0,0,1,
		0,1,1,
		0,1,0,
		0,0,0,
	};

	public static byte[] rightFace  = new byte[]{
			/* 1, 0, 1,
	    1, 0, 0,
	    1, 1, 0,
	    1, 1, 1,*/
		1,0,0,
		1,1,0,
		1,1,1,
		1,0,1,
	};

	public static byte[] topFace  = new byte[]{
			/* 0, 1, 1,
	    1, 1, 1,
	    1, 1, 0,
	    0, 1, 0,*/
		0,1,0,
		0,1,1,
		1,1,1,
		1,1,0,
	};

	public static byte[] bottomFace  = new byte[]{
			/* 0, 0, 0,
	    1, 0, 0,
	    1, 0, 1,
	    0, 0, 1 */
		0,0,1,
		0,0,0,
		1,0,0,
		1,0,1,
	};
	
	public static byte[] textures = new byte[] {
		-1,-1,
		-1, 1,
		 1, 1,
		 1,-1,
			
	};
	
	public static byte[] sharedNormals = new byte[] {
		-1,-1, 1,
		-1, 1, 1,
		 1, 1, 1,
		 1,-1, 1,
		 
		 1,-1, -1,
		 1, 1, -1,
		-1, 1, -1,
		-1,-1,-1	
	};
	
	public static byte[][] faces = new byte[][]{
		frontFace,
		backFace,
		leftFace,
		rightFace, 
		topFace,
		bottomFace
	};
	
	public static int[] indices = new int[] {
		0, 1, 2, 0, 2, 3	
	};
	
	private static final byte [][] normals = new byte[][] {
		new byte[]{
			0,0,-1
		},
		new byte[] {
			0,0,1
		},
		new byte[] {
			-1,0,0
		},
		new byte[] {
			1,0,0
		},
		new byte[] {
			0,1,0
		},
		new byte[] {
			0,-1,0
		},
	};
	/*public static byte[] getAllVertices(int x, int y, int z) {
		byte [] allVertices = new byte[frontFace.length * 6];
		int step = frontFace.length;
		System.arraycopy(frontFace, 0, allVertices, step * 0, step);
		System.arraycopy(backFace, 	0, allVertices, step * 1, step);
		System.arraycopy(leftFace, 	0, allVertices, step * 2, step);
		System.arraycopy(rightFace, 0, allVertices, step * 3, step);
		System.arraycopy(topFace,	0, allVertices, step * 4, step);
		System.arraycopy(bottomFace,0, allVertices, step * 5, step);
		
		for(int i = 0 ; i < allVertices.length; i++) {
			if(i % 3 == 0)
				allVertices[i] += x;
			else if(i % 3 == 1)
				allVertices[i] += y;
			else if(i % 3 == 2)
				allVertices[i] += z;
		}
		return allVertices;
	}*/
	
	private static int[] newIndices = new int[6];
	
	public static int[] getIndices(int offset) {
		
		for(int i = 0; i < indices.length; i++) {
			newIndices[i] = indices[i]+offset;
		}
		return newIndices;
	}
	
	private static byte[][] newFaces = new byte[6][frontFace.length];
	
	public static byte[][] getAllFaces(int x, int y, int z) {
		
		for(int i =0 ; i < faces.length; i++) {
			for(int j = 0; j < faces[0].length; j++) {
				newFaces[i][j] = faces[i][j];
				if(j % 3 == 0)
					newFaces[i][j] += x;
				else if(j % 3 == 1)
					newFaces[i][j] += y;
				else if(j % 3 == 2)
					newFaces[i][j] += z;
			}
		}
		return newFaces;
	}
	
	public static byte[][] getNormals() {
		
		return normals;
	}
}
