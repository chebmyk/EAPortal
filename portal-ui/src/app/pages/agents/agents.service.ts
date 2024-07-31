import {Injectable, NgZone} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Agent, FsNode} from "../../model/agents";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AgentsService {

  constructor(
    private http: HttpClient,
    private _zone: NgZone
  ) { }

  getAgents(): Observable<Agent[]> {
    return  this.http.get<Agent[]>(`http://localhost:8081/zookeeper/instance`);

    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

  getFileTree(): Observable<FsNode> {
    return  this.http.get<FsNode>(`http://localhost:8081/fs/filetree`);

    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }

  getFile(filepath: string): Observable<any> {
    return  this.http.post(
      "http://localhost:8083/fs/filestream", //`http://localhost:8081/fs/file`,
      {file: filepath}
    );
  }

  getSSE(url: string): Observable<any> {
    return new Observable(observer => {
      let eventSource = new EventSource(url);

      // eventSource.addEventListener(topic, event => {
      //   observer.next(event);
      // })
      //
      // return () => eventSource.close();

      eventSource.onmessage = event => {
        this._zone.run(() => {
          observer.next(event);
        });
      };

      eventSource.onerror = error => {
        this._zone.run(() => {
          observer.error(error);
        });
      };

      // return () => eventSource.close();

    });

  }

}

