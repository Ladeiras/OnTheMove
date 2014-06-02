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
    public class ServicoController : ApiController
    {
        //
        // GET: /Servico/

        public Servico Get(string id,string serv)
        {
            return Lib_Servico.GetServico(id,serv);
        }

    }
}
