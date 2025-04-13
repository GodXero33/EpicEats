import { UserRole } from "./user-role.enum";

export type User = {
	employeeId: number;
	username: string;
	password: string;
	createdAt: string;
	updatedAt: string;
	lastLogin: string;
	role: UserRole;
};
