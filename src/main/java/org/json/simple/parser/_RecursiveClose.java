package org.json.simple.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class _RecursiveClose implements AutoCloseable
{
    private HashSet<Object> _inProcessObj = new HashSet<Object>();
        
    public void close(Object closeThis) throws Exception
    {
        if(null==closeThis || _inProcessObj.contains(closeThis))
            return;
        _inProcessObj.add(closeThis);

        try {
            if(closeThis instanceof AutoCloseable) {
                _closeAuto((AutoCloseable) closeThis);
            }

            if(closeThis instanceof Map<?,?>) {
                _closeMap((Map<?,?>) closeThis);
            } else if(closeThis instanceof Collection<?>) {
                _closeCol((Collection<?>) closeThis);
            }
        } finally {
            _inProcessObj.remove(closeThis);
        }
    }

    private void _closeAuto(AutoCloseable autoClose) throws Exception
    {
        if(_inProcessObj.contains(autoClose))
            return;
        _inProcessObj.add(autoClose);
        try {
            autoClose.close();
        } finally {
            _inProcessObj.remove(autoClose);
        }
        
    }

    private void _closeCol(Collection<?> closeCollection) throws Exception
    {
        if(_inProcessObj.contains(closeCollection))
            return;
        _inProcessObj.add(closeCollection);
        try {
            while(closeCollection.size()>0)
            {
                Object obj = closeCollection.remove(0);
                close(obj);
                obj = null;
            }
            closeCollection.clear();
        } finally {
            _inProcessObj.remove(closeCollection);
        }
    }

    private void _closeMap(Map<?,?> closeMap) throws Exception
    {
        if(_inProcessObj.contains(closeMap))
            return;
        _inProcessObj.add(closeMap);
        try {
            for(Map.Entry<?, ?> kv:closeMap.entrySet())
            {
                close(kv.getKey());
                close(kv.getValue());
            }
            closeMap.clear();
        } finally {
            _inProcessObj.remove(closeMap);
        }
    }

    @Override
    public void close() throws Exception { _inProcessObj.clear(); _inProcessObj = null; }
}
