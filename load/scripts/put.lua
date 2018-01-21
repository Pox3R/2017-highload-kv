-- example dynamic request script which demonstrates changing
-- the request path and a header for each request
-------------------------------------------------------------
-- NOTE: each wrk thread has an independent Lua scripting
-- context and thus there will be one counter per thread

wrk.method = "PUT"
cnt = 0
charCnt = math.random(97,122)

request = function()
   path = "/v0/entity?id=" .. cnt
   d = string.char(charCnt)
   for i = 1, 8192 do
      d = d .. string.char(charCnt)
	  if (charCnt == 122) then 
		charCnt = 97
	  else 
		charCnt = charCnt + 1
	  end
   end
   wrk.body = d
   cnt = cnt + 1
   return wrk.format(nil, path)
end
