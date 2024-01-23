import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class VisibilityService {
    private liveboardVisibilitySubject = new BehaviorSubject<boolean>(true);
    liveboardVisibility$: Observable<boolean> = this.liveboardVisibilitySubject.asObservable();

    private formVisibilitySubject = new BehaviorSubject<boolean>(true);
    formVisibility$: Observable<boolean> = this.liveboardVisibilitySubject.asObservable();

    setLiveboardVisibility(value: boolean): void {
        this.liveboardVisibilitySubject.next(value);
    }

    setFormVisibility(value: boolean): void {
        this.formVisibilitySubject.next(value);
    }
}