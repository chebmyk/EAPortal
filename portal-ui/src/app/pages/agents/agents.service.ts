import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Agent} from "../../model/agents";
import {Observable} from "rxjs";
import {config} from "../../app.config";

@Injectable({
  providedIn: 'root'
})
export class AgentsService {

  constructor(
    private http: HttpClient
  ) { }

  getAgents(): Observable<Agent[]> {
    return  this.http.get<Agent[]>(`http://localhost:8081/zookeeper/instance`);

    // return  this.http.get<Agent[]>(`${config.apiUrl}/zookeeper/instance`);
  }
}

