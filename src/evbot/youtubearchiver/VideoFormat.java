package evbot.youtubearchiver;

public enum VideoFormat {
	
	original(38, "mp4"),
	p1080(37, "mp4"),
	p720(22, "mp4"),
	p480(35, "flv"),
	p360(18, "mp4"),
	p360f(34, "flv"),
	p240(5, "flv");
	
	private int id;
	private String format;
	
	private VideoFormat(int id, String format) {
		this.id = id;
		this.format = format;
	}
	
	public int getID() {
		return id;
	}
	
	public String getFormat() {
		return format;
	}
}
