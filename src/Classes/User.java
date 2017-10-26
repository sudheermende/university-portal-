package Classes;

public class User extends UserGuiDB {
private String userId;
private byte[] encryptUserId;


public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public byte[] getEncryptUserId() {
	return encryptUserId;
}
public void setEncryptUserId(byte[] encryptUserId) {
	this.encryptUserId = encryptUserId;
}

}
