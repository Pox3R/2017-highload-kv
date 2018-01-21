
wrk.method = "GET"
cnt = 0
modCnt = 684

request = function()
   path = "/v0/entity?id=" .. cnt
   cnt = (cnt + 1) % modCnt
   return wrk.format(nil, path)
end
