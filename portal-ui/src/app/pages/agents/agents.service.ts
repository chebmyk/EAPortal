import {Injectable, NgZone} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Agent} from "../../model/agents";
import {Observable} from "rxjs";
import {config} from "../../app.config";

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

  getMemoryStream(url: string): Observable<any> {
    url = "http://localhost:8083/zookeeper/stream/memory"

    return new Observable(observer => {
      const eventSource = new EventSource(url);
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
    });
  }

}

