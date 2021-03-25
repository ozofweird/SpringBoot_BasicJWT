### 에러
```
There is no PasswordEncoder mapped for the id “null”
```
Spring Security 5.x 이상부터는 PasswordEncoder가 변경되었기 때문에 password 앞에 식별자 정보를 넣어주어야합니다.
이전에는 noopPasswordEncoder였지만, 기본전략이 {noop}에서 bycrpt 방식으로 변 (NoOpPasswordEncoder는 deprecated)
* Sha256, scrypt 등 id에 해당하는 알고리즘 명을 주고, 실제 패스워드 인코더 객체를 설정해주면, 알고리즘에 해당하는 패스워드 인코드를 사용한다.
* SpringSecurity 5 에서 기본 패스워드 인코더로 사용하는 bcrypt 방식을 암호화를 사용하는 PasswordEncoder를 사용해보자

기본적인 해결방법으로는, 입력받은 password 앞에 '{noop}'키워드를 넣는 방법이다. 
```
{noop}password
```
createDelegatingPasswordEncoder() - Bcyrpt PasswordEncoder를 Bean 등록

``` java
public static PasswordEncoder createDelegatingPasswordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(encodingId, new BCryptPasswordEncoder());
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256",
				new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		encoders.put("argon2", new Argon2PasswordEncoder());
		return new DelegatingPasswordEncoder(encodingId, encoders);
	}
```
