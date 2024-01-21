import { Observable } from 'rxjs';

export type ParamsType = { hideLoader: boolean };

export interface IApiBaseActions {
  Get(url: string, params?: ParamsType): Observable<any>;

  Post(url: string, data: any, params?: ParamsType): Observable<any>;

  Delete(url: string, data?: any, params?: ParamsType): Observable<any>;

  Put(url: string, data: any, params?: ParamsType): Observable<any>;
}

export interface IApiBaseResponse {
  success: boolean,
  version: string;
  timestamp: number;
  data: any;
  message?: string;
}
