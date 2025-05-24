class EmployeeShiftBuilder {
	private employee: EmployeeShift;

	constructor (employee: EmployeeShift) {
		this.employee = employee;
	}

	public id (id: number): EmployeeShiftBuilder {
		this.employee.id = id;
		return this;
	}

	public shiftDate (shiftDate: string): EmployeeShiftBuilder {
		this.employee.shiftDate = shiftDate;
		return this;
	}

	public startTime (startTime: string): EmployeeShiftBuilder {
		this.employee.startTime = startTime;
		return this;
	}

	public endTime (endTime: string): EmployeeShiftBuilder {
		this.employee.endTime = endTime;
		return this;
	}

	public build (): EmployeeShift {
		return this.employee;
	}
}

export class EmployeeShift {
	public id!: number;
	public shiftDate!: string;
	public startTime!: string;
	public endTime!: string;

	public static builder () {
		return new EmployeeShiftBuilder(new EmployeeShift());
	}
}
