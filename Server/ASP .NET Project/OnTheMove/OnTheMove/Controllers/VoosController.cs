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
    public class VoosController : ApiController
    {
        //
        // GET: /Voos/

        public Voo Get(string id)
        {
            return Lib_Voo.GetVoo(id);
        }

    }
}
