import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class VisibilityService {
    private visibilitySubject = new BehaviorSubject<boolean>(true);
    visibility$: Observable<boolean> = this.visibilitySubject.asObservable();

    setVisibility(value: boolean): void {
        this.visibilitySubject.next(value);
    }
}