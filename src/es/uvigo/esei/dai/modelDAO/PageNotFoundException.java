package es.uvigo.esei.dai.modelDAO;

public class PageNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private final String uuid;

	public PageNotFoundException(String uuid) {
		this.uuid = uuid;
	}

	public PageNotFoundException(String message, String uuid) {
		super(message);
		this.uuid = uuid;
	}

	public PageNotFoundException(Throwable cause, String uuid) {
		super(cause);
		this.uuid = uuid;
	}

	public PageNotFoundException(String message, Throwable cause, String uuid) {
		super(message, cause);
		this.uuid = uuid;
	}

	public PageNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace, String uuid) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.uuid = uuid;
	}
	
	public String getId() {
		return this.uuid;
	}
}
