package cc.sukazyo.icee.util.file;

public abstract class FileTreeException extends Exception {
	
	public FileTreeException (String message) {
		super(message);
	}
	
	public static class FileNameUnavailableException extends FileTreeException {
		
		public FileNameUnavailableException (String filename, int location, String reason) {
			super(String.format("Unavailable filename %s, error at %d, due to: %s", filename, location, reason));
		}
		
	}
	
	public static class ConflictException extends FileTreeException {
		
		public ConflictException () {
			super("");
		}
		
	}
	
}
