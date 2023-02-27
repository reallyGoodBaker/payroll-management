package utils;

import java.util.ArrayList;
import java.util.List;

public class Tries {
    public static Tryment flow(Attempt attempt) {
        return new TrymentImpl(attempt);
    }

    public record TryResult(
        boolean ok,
        Object res
    ) {}

    public static interface Tryment {
        Tryment then(Attempt attempt);
        Tryment caught(Attempt attempt);
        void start();
    }

    static class TrymentImpl implements Tryment {

        private List<Attempt> onSucCallbacks = new ArrayList<>();
        private List<Attempt> onErrCallbacks = new ArrayList<>();
        private final Attempt attempt;

        TrymentImpl(Attempt attempt) {
            this.attempt = attempt;
        }

        static interface _Callback {
            void run(TryResult res);
        }

        private void _callAttemptWithResult(Attempt attempt, _Callback callback) {

            Object result;
            boolean ok;

            try {
                result = attempt.run(null);
                ok = true;
            } catch (Exception e) {
                result = e;
                ok = false;
            }

            callback.run(new TryResult(ok, result));

        }

        private void _invokeAttempt(Attempt attempt) {
            _callAttemptWithResult(attempt, val -> {
                
                if (val.ok) {
                    succeed(this, val.res);
                    return;
                }
    
                failed(this, (Exception) val.res);
            });
        }

        static void succeed(TrymentImpl tryment, Object val) {
            Object prev = val;
            for (Attempt attempt : tryment.onSucCallbacks) {
                try {
                    prev = attempt.run(prev);
                } catch (Exception e) {
                    failed(tryment, e);
                    break;
                }
            }
        }

        static void failed(TrymentImpl tryment, Exception e) {
            Exception reason = e;
            for (Attempt attempt : tryment.onErrCallbacks) {
                try {
                    attempt.run(reason);
                    return;
                } catch (Exception er) {
                    reason = er;
                    continue;
                }
            }
            
            reason.printStackTrace();
        }

        public Tryment then(Attempt attempt) {
            onSucCallbacks.add(attempt);
            return this;
        }

        public Tryment caught(Attempt attempt) {
            onErrCallbacks.add(attempt);
            return this;
        }

        public void start() {
            _invokeAttempt(attempt);
        }

    }

}
