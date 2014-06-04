using OnTheMove.Lib;
using OnTheMove.Lib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace OnTheMove.Controllers
{
    public class LojasController : ApiController
    {
        //
        // GET: /Lojas/

        public IEnumerable<Loja> Get(string id)
        {
            return Lib_Loja.ListLojas(id);
        }

    }
}
