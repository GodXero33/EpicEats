import { Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { SignupComponent } from './component/signup/signup.component';
import { HomeComponent } from './component/home/home.component';
import { AuthGuard } from './service/auth-gard.service';
import { EmployeeComponent } from './component/employee/employee.component';
import { OrderComponent } from './component/order/order.component';
import { InventoryComponent } from './component/inventory/inventory.component';
import { FinanceComponent } from './component/finance/finance.component';
import { MerchandiseComponent } from './component/merchandise/merchandise.component';
import { RestaurantComponent } from './component/restaurant/restaurant.component';
import { SettingsComponent } from './component/settings/settings.component';
import { AddEmployeeComponent } from './component/employee/add-employee/add-employee.component';
import { UpdateEmployeeComponent } from './component/employee/update-employee/update-employee.component';
import { TerminateEmployeeComponent } from './component/employee/terminate-employee/terminate-employee.component';
import { SearchEmployeeComponent } from './component/employee/search-employee/search-employee.component';
import { AddEmployeeShiftComponent } from './component/employee/add-employee-shift/add-employee-shift.component';
import { UpdateEmployeeShiftComponent } from './component/employee/update-employee-shift/update-employee-shift.component';
import { DeleteEmployeeShiftComponent } from './component/employee/delete-employee-shift/delete-employee-shift.component';
import { SearchEmployeeShiftComponent } from './component/employee/search-employee-shift/search-employee-shift.component';
import { UpdateEmployeePromotionComponent } from './component/employee/update-employee-promotion/update-employee-promotion.component';
import { DeleteEmployeePromotionComponent } from './component/employee/delete-employee-promotion/delete-employee-promotion.component';
import { SearchEmployeePromotionComponent } from './component/employee/search-employee-promotion/search-employee-promotion.component';
import { SearchAllEmployeesComponent } from './component/employee/search-employee/search-all-employees/search-all-employees.component';
import { SearchEmployeeByIdComponent } from './component/employee/search-employee/search-employee-by-id/search-employee-by-id.component';

export const routes: Routes = [
	{
		path: '',
		component: LoginComponent
	},
	{
		path: 'login',
		component: LoginComponent
	},
	{
		path: 'signup',
		component: SignupComponent
	},
	{
		path: 'home',
		component: HomeComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'employee',
		component: EmployeeComponent,
		canActivate: [AuthGuard],
		data: { adminOnly: true },
		children: [
			{
				path: '',
				component: AddEmployeeComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'add',
				component: AddEmployeeComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'update',
				component: UpdateEmployeeComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'terminate',
				component: TerminateEmployeeComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'search',
				component: SearchEmployeeComponent,
				canActivate: [AuthGuard],
				children: [
					{
						path: '',
						component: SearchEmployeeByIdComponent,
						canActivate: [AuthGuard]
					},
					{
						path: 'all',
						component: SearchAllEmployeesComponent,
						canActivate: [AuthGuard]
					},
					{
						path: 'by-id',
						component: SearchEmployeeByIdComponent,
						canActivate: [AuthGuard]
					}
				]
			},
			{
				path: 'shift-add',
				component: AddEmployeeShiftComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'shift-update',
				component: UpdateEmployeeShiftComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'shift-delete',
				component: DeleteEmployeeShiftComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'shift-search',
				component: SearchEmployeeShiftComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'promotion-update',
				component: UpdateEmployeePromotionComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'promotion-delete',
				component: DeleteEmployeePromotionComponent,
				canActivate: [AuthGuard]
			},
			{
				path: 'promotion-search',
				component: SearchEmployeePromotionComponent,
				canActivate: [AuthGuard]
			}
		]
	},
	{
		path: 'order',
		component: OrderComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'inventory',
		component: InventoryComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'finance',
		component: FinanceComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'merchandise',
		component: MerchandiseComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'restaurant',
		component: RestaurantComponent,
		canActivate: [AuthGuard]
	},
	{
		path: 'settings',
		component: SettingsComponent,
		canActivate: [AuthGuard]
	}
];
