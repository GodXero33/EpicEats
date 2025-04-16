import { EmployeeRole } from "../../enum/employee-role.enum";

class EmployeeBuilder {
	private employee: Employee;

	constructor (employee: Employee) {
		this.employee = employee;
	}

	public id (id: number): EmployeeBuilder {
		this.employee.id = id;
		return this;
	}

	public name (name: string): EmployeeBuilder {
		this.employee.name = name;
		return this;
	}

	public phone (phone: string): EmployeeBuilder {
		this.employee.phone = phone;
		return this;
	}

	public email (email: string): EmployeeBuilder {
		this.employee.email = email;
		return this;
	}

	public address (address: string): EmployeeBuilder {
		this.employee.address = address;
		return this;
	}

	public salary (salary: number): EmployeeBuilder {
		this.employee.salary = salary;
		return this;
	}

	public role (role: EmployeeRole): EmployeeBuilder {
		this.employee.role = role;
		return this;
	}

	public dob (dob: string): EmployeeBuilder {
		this.employee.dob = dob;
		return this;
	}

	public employeeSince (employeeSince: string): EmployeeBuilder {
		this.employee.employeeSince = employeeSince;
		return this;
	}

	public build (): Employee {
		return this.employee;
	}
}

export class Employee {
	public id!: number;
	public name!: string;
	public phone!: string;
	public email!: string;
	public address!: string;
	public salary!: number;
	public role!: EmployeeRole;
	public dob!: string;
	public employeeSince!: string;

	public static builder () {
		return new EmployeeBuilder(new Employee());
	}
}
