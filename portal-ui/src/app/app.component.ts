import {Component, OnInit} from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {CommonModule} from "@angular/common";
import {MenuItem} from "primeng/api";
import {MenuModule} from "primeng/menu";
import {AvatarModule} from "primeng/avatar";


@Component({
  selector: 'app-root',
  standalone: true,
    imports: [
        RouterOutlet,
        CommonModule,
        RouterLink,
        RouterLinkActive,
        MenuModule,
        AvatarModule
    ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{

  title = 'portal-ui';
  mainMenu:  MenuItem[] | undefined;

  constructor(
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.mainMenu = [
      {
        // label: 'Menu',
        items: [
          {
            label: 'Agents',
            icon: 'pi pi-microchip',
            command: () => {
                  this.router.navigate(['/agents']);
            }
          },
          // {
          //   label: 'Applications',
          //   icon: 'pi pi-link',
          //   command: () => {
          //     this.router.navigate(['/installation']);
          //   }
          // }
        ]
      }
    ];
  }
}
