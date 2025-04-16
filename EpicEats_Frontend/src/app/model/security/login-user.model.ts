class LoginUserBuilder {
	private user: LoginUser;

	constructor (user: LoginUser) {
		this.user = user;
	}

	public username (username:string): LoginUserBuilder {
		this.user.username = username;
		return this;
	}

	public password (password:string): LoginUserBuilder {
		this.user.password = password;
		return this;
	}

	public build (): LoginUser {
		return this.user;
	}
}

export class LoginUser {
	public username!: string;
	public password!: string;

	public static builder (): LoginUserBuilder {
		return new LoginUserBuilder(new LoginUser());
	}
}
