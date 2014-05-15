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
    public class AeroportosController : ApiController
    {
        //
        // GET: /Aeroportos/

        public IEnumerable<Aeroporto> Get()
        {
            return Lib_Aeroporto.ListAeroportos();
        }

    }
}
