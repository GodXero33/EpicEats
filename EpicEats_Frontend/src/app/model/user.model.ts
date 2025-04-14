import { UserRole } from "./user-role.enum";

class UserBuilder {
	private user: User;

	constructor (user: User) {
		this.user = user;
	}

	public employeeId (employeeId: number): UserBuilder {
		this.user.employeeId = employeeId;
		return this;
	}

	public username (username: string): UserBuilder {
		this.user.username = username;
		return this;
	}

	public password (password: string): UserBuilder {
		this.user.password = password;
		return this;
	}

	public createdAt (createdAt: string): UserBuilder {
		this.user.createdAt = createdAt;
		return this;
	}

	public updatedAt (updatedAt: string): UserBuilder {
		this.user.updatedAt = updatedAt;
		return this;
	}

	public lastLogin (lastLogin: string): UserBuilder {
		this.user.lastLogin = lastLogin;
		return this;
	}

	public role (role: UserRole): UserBuilder {
		this.user.role = role;
		return this;
	}

	public build (): User {
		return this.user;
	}
}

export class User {
	public employeeId!: number;
	public username!: string;
	public password!: string;
	public createdAt!: string;
	public updatedAt!: string;
	public lastLogin!: string;
	public role!: UserRole;

	public static builder (): UserBuilder {
		return new UserBuilder(new User());
	}
}
