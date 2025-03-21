import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-manage-users',
  standalone: false,
  templateUrl: './manage-users.component.html',
  styleUrl: './manage-users.component.css'
})
export class ManageUsersComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  // Load users from backend
  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (error) => {
        console.error('❌ Error fetching users:', error);
      }
    });
  }

  // Delete user
  deleteUser(userId: number) {
    if (confirm("Are you sure you want to delete this user?")) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.id !== userId);
          alert("✅ User deleted successfully!");
        },
        error: (error) => {
          console.error('❌ Error deleting user:', error);
          alert("❌ Failed to delete user.");
        }
      });
    }
  }

  // Change user role
  changeUserRole(user: User) {
    if (!confirm(`Are you sure you want to change ${user.name}'s role to ${user.role}?`)) {
      return;
    }

    this.userService.updateUserRole(user.id, user.role).subscribe({
      next: () => {
        alert(`✅ User role updated successfully to ${user.role}!`);
        this.loadUsers(); // Refresh the list after update
      },
      error: (error) => {
        console.error('❌ Failed to update user role:', error);
        alert('❌ Failed to update user role.');
      }
    });
  }

  changeUserStatus(user: any) {
    this.userService.updateUserStatus(user.id, user.status).subscribe({
      next: (response: any) => {  // <-- Use 'any' to bypass TypeScript's type checking
        console.log("✅ Success Response:", response);
        alert(response.message); // Accessing 'message' directly
      },
      error: (error) => {
        console.error("❌ Error Response:", error);
        alert(`❌ Failed to update user status: ${error.message}`);
      }
    });
  }
  
  
  

  

  
}





