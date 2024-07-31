import {Component, OnInit} from '@angular/core';
import {TreeModule, TreeNodeSelectEvent} from "primeng/tree";
import {TreeNode} from "primeng/api";
import {AgentsService} from "../../agents.service";
import {FsNode} from "../../../../model/agents";
import {JsonPipe} from "@angular/common";

@Component({
  selector: 'app-agent-info',
  standalone: true,
  imports: [
    TreeModule,
    JsonPipe
  ],
  templateUrl: './agent-info.component.html',
  styleUrl: './agent-info.component.scss'
})
export class AgentInfoComponent implements OnInit {
  files!: TreeNode[];

  selectedFile!: TreeNode;
  filecontent: string ="";

  constructor(
    private agentService: AgentsService
  ) {
  }

  ngOnInit() {
     this.agentService.getFileTree()
      .subscribe(node => {
        this.files= [this.mapNodeToTreeNode(node)];
      });
  }

  mapNodeToTreeNode(node: FsNode): TreeNode {
  const treeNode: TreeNode = {
    key: node.path,
    label: node.name,
    data: node,
    children: node.children ? node.children.map(n => this.mapNodeToTreeNode(n)) : []
  };

  return treeNode;
}

  nodeSelect($event: TreeNodeSelectEvent) {
    console.log($event);
    const file = $event.node.data as FsNode;
    if (!file.isDirectory) {
      this.filecontent = '';
      this.agentService.getFile(file.path)   //.getFile(encodeURIComponent(file.path))
        .subscribe(line => {
          this.filecontent += line;
        });
    }
  }
}
