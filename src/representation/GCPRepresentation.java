package representation;

public class GCPRepresentation implements Representation {
	public int[] colors;
	
	public GCPRepresentation(int vertexcount) {
		colors = new int[vertexcount];
	}
	
	public GCPRepresentation(int[] colors) {
		this.colors = colors;
	}
	@Override
	public Representation copy() {
		int[] clone = new int[colors.length];
		for (int i=0;i<clone.length;i++)
			clone[i] = colors[i];
		
		return new GCPRepresentation(clone);
	}
	
	public int getColor(int vertex) {
		if (vertex>=colors.length) {
			throw new IndexOutOfBoundsException();
		}
		return colors[vertex];
	}
	
	@Override
	public String toString() {
		String res = "Colors:";
		for (int i=0;i<colors.length;i++) {
			res += " "+colors[i];
		}
		return res;
	}

}
