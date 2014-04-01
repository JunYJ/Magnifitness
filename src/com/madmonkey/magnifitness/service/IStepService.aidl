package com.madmonkey.magnifitness.service;

import com.madmonkey.magnifitness.service.IStepServiceCallback;

interface IStepService {
		boolean isRunning();
		void setSensitivity(int sens);
		void registerCallback(IStepServiceCallback cb);
		void unregisterCallback(IStepServiceCallback cb);
}