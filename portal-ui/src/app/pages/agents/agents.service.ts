import {Injectable, NgZone} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Agent} from "../../model/agents";
import {BehaviorSubject, Observable} from "rxjs";
import {fetchEventSource} from "@microsoft/fetch-event-source";
import {FsNode, FsAttributes} from "../../model/file-system";
import {config} from "../../app.config";
import {SseService} from "../../services/sse.service";
import {FetchService} from "../../services/fetch.service";

@Injectable({
  providedIn: 'root'
})
export class AgentsService {

  eventStream$ = new BehaviorSubject([]);
  eventSource!: EventSource;

  constructor(
    private sse: SseService,
    private fetch: FetchService,
    private http: HttpClient,
    private _zone: NgZone

  ) { }

  getAgents(): Observable<Agent[]> {
    return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

  getAgent(id: string): Observable<Agent> {
    return  this.http.get<any>(`${config.apiUrl}/zookeeper/instance/${id}`);
  }


  getFileTree(agentId: string): Observable<FsNode> {
    console.log("AgentId", agentId)
    return  this.http.get<FsNode>(`${config.apiUrl}/fs/${agentId}/filetree`);
  }


  getFileMetaData(agentId: string, filepath: string): Observable<FsAttributes> {
    return  this.http.post<FsAttributes>(`${config.apiUrl}/fs/${agentId}/filemetadata`, {file: filepath });
  }


  getAgentSystemLoad(agentId: string): Observable<any> {
    return this.sse.getServerEvents(`${config.apiUrl}/stream/${agentId}/systemload`)
  }


  getFileContent(agentId: string, filepath: string,
    onEvent: (data:any) => void,
    onError?: ((error: any) => void),
    onComplete?: () => void,
  ) {
    let body = { "file": filepath };
    return this.fetch.post(`${config.apiUrl}/fs/${agentId}/filestream`,
      body,
      onEvent,
      onError,
      onComplete
    )
  }

}
