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
		data: { adminOnly: true }
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
