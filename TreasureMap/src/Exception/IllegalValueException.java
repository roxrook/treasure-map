/*
 * @author : Chan Nguyen 
 */
package Exception;

public class IllegalValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IllegalValueException(String message) {
		super(message);
	}
}
